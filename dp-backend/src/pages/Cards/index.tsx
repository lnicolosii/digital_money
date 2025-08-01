import React, { useState, useMemo, useEffect } from "react";
import { usePagination } from "../../hooks/usePagination";
import {
  CardCustom,
  ErrorMessage,
  Errors,
  Icon,
  RecordProps,
  Records,
  RecordVariant,
  IRecord,
  Skeleton,
  SkeletonVariant,
  SnackBar,
} from "../../components";
import { Button, Pagination, PaginationItem } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import {
  ROUTES,
  ADD,
  SELECT,
  SUCCESS,
  CARDS_PLACEHOLDERS,
  SUCCESS_MESSAGES,
  SUCCESS_MESSAGES_KEYS,
  MESSAGE,
  UNAUTHORIZED,
} from "../../constants";
import { useSearchParams } from "react-router-dom";
import Cards, { Focused } from "react-credit-cards-2";
import "react-credit-cards-2/dist/es/styles-compiled.css";
import { FieldValues, SubmitHandler, useForm } from "react-hook-form";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import OutlinedInput from "@mui/material/OutlinedInput";
import {
  handleChange,
  nameValidationConfig,
  cardValidationConfig,
  expirationValidationConfig,
  cvcValidationConfig,
  isValueEmpty,
  transformExpiration,
  valuesHaveErrors,
  getUserCards,
  parseRecordContent,
  createUserCard,
  pageQuery,
  getAccount,
  isVisa,
  isMastercard,
} from "../../utils/";
import { Card } from "../../types";
import { useUserInfo, useLocalStorage, useAuth } from "../../hooks";

const recordsPerPage = 10;
const duration = 2000;

const CardsComponent = () => {
  const [userCards, setUserCards] = useState<IRecord[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const { pageNumber, numberOfPages, isRecordsGreeterThanOnePage } =
    usePagination(userCards as RecordProps[], recordsPerPage);
  const [searchParams] = useSearchParams();
  const isAdding = !!searchParams.get("add");
  const isSelecting = !!searchParams.get("select");
  const isSuccess = !!searchParams.get("success");
  const message = (searchParams.get("message") as SUCCESS_MESSAGES_KEYS) || "";
  const [token] = useLocalStorage("token");
  const { logout } = useAuth();
  const navigate = useNavigate();
  const { user } = useUserInfo();

  useEffect(() => {
    if (!isAdding) {
      if (user && user.id) {
        getAccount(user.id, token).then((account) => {
          getUserCards(account.id, token)
            .then((cards) => {
              if ((cards as Card[]).length > 0) {
                const parsedRecords = (cards as Card[]).map(
                  (parsedCard: Card) =>
                    parseRecordContent(parsedCard, RecordVariant.CARD)
                );
                setUserCards(parsedRecords);
              }
              if (isSuccess) {
                setTimeout(() => navigate(ROUTES.CARDS), duration);
              }
            })
            .finally(() => setIsLoading(false))
            .catch((error) => {
              if (error.status === UNAUTHORIZED) {
                logout();
              }
            });
        });
      }
    }
  }, [isAdding, isSuccess, logout, navigate, token, user]);

  return (
    <div className="tw-w-full">
      {!isAdding ? (
        <>
          <CardCustom
            className="tw-max-w-5xl"
            content={
              <div className="tw-flex tw-justify-between tw-mb-4">
                <p className="tw-font-bold">
                  Agregá tu tarjeta de débito o crédito
                </p>
              </div>
            }
            actions={
              <Link
                to={`${ROUTES.CARDS}?${ADD}`}
                className="tw-w-full tw-flex tw-items-center tw-justify-between tw-p-4 hover:tw-bg-neutral-gray-500 tw-transition"
              >
                <div className="tw-flex tw-items-center tw-gap-x-4">
                  <Icon type="add" />
                  <p>Nueva tarjeta</p>
                </div>
                <Icon type="arrow-right" />
              </Link>
            }
          />
          <CardCustom
            className="tw-max-w-5xl"
            content={
              <>
                <div>
                  <p className="tw-mb-4 tw-font-bold">Tus tarjetas</p>
                </div>
                {userCards.length > 0 && !isLoading && (
                  <Records
                    records={userCards}
                    initialRecord={pageNumber * recordsPerPage - recordsPerPage}
                    setRecords={setUserCards}
                    isSelecting={isSelecting}
                    onSelect={(cardId: string) => {
                      navigate(`${ROUTES.LOAD_MONEY}?card&cardId=${cardId}`);
                    }}
                  />
                )}
                {userCards.length === 0 && !isLoading && (
                  <p>No hay tarjetas asociadas</p>
                )}
                {isLoading && (
                  <Skeleton
                    variant={SkeletonVariant.RECORD_LIST}
                    numberOfItems={5}
                  />
                )}
              </>
            }
            actions={
              isRecordsGreeterThanOnePage && (
                <div className="tw-h-12 tw-w-full tw-flex tw-items-center tw-justify-center tw-px-4 tw-mt-4">
                  <Pagination
                    count={numberOfPages}
                    shape="rounded"
                    renderItem={(item) => (
                      <PaginationItem
                        component={Link}
                        to={pageQuery(ROUTES.CARDS, item.page as number)}
                        {...item}
                      />
                    )}
                  />
                </div>
              )
            }
          />
        </>
      ) : (
        <CardForm />
      )}
      {isSuccess && (
        <SnackBar
          duration={3000}
          message={SUCCESS_MESSAGES[message] ? SUCCESS_MESSAGES[message] : ""}
          type="success"
        />
      )}
    </div>
  );
};

export default CardsComponent;

function CardForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isDirty },
  } = useForm({
    criteriaMode: "all",
  });
  const [formState, setFormState] = useState<{
    number: string;
    name: string;
    expiry: string;
    cvc: string;
    focused?: string | undefined;
  }>({
    number: "",
    name: "",
    expiry: "",
    cvc: "",
    focused: undefined,
  });
  const [isError, setIsError] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>("");
  const isEmpty = isValueEmpty(formState);
  const hasErrors = useMemo(() => valuesHaveErrors(errors), [errors]);
  const navigate = useNavigate();
  const { user } = useUserInfo();
  const [token] = useLocalStorage("token");
  const { logout } = useAuth();

  const onChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    maxLength?: number
  ) => handleChange(event, setFormState, maxLength);

  const handleFocus = (event: React.FocusEvent<HTMLInputElement>) => {
    setFormState({ ...formState, focused: event.target.name });
  };

  const onSubmit: SubmitHandler<FieldValues> = (data) => {
    const { expiry, number, name, cvc } = data;

    const isVisaCard = isVisa(number);
    const isMasterCard = isMastercard(number);
    const network = isVisaCard
      ? "visa"
      : isMasterCard
      ? "mastercard"
      : "random";
    transformExpiration(expiry);
    if (user && user.id) {
      getAccount(user.id, token).then((account) => {
        createUserCard(
          {
            accountId: account.id,
            expirationDate: expiry,
            number,
            holder: name,
            cvv: cvc,
            cardType: "credit",
            network,
            bank: "BBVA",
          },
          token
        )
          .then(() => {
            navigate(
              `${ROUTES.CARDS}?${SUCCESS}&${MESSAGE}${SUCCESS_MESSAGES_KEYS.CARD_ADDED}`
            );
          })
          .catch((error) => {
            setIsError(true);
            setErrorMessage(error.statusText as string);
            if (error.status === UNAUTHORIZED) {
              setTimeout(() => logout(), duration);
            }
          });
      });
    }
  };

  return (
    <>
      <CardCustom
        className="tw-max-w-5xl"
        content={
          <div className="tw-flex tw-flex-col" id="PaymentForm">
            <div className="tw-flex tw-justify-between tw-mb-4">
              <p className="tw-font-bold">Agregá una nueva tarjeta</p>
            </div>
            <div className="tw-mb-5">
              <Cards
                cvc={formState.cvc}
                expiry={formState.expiry}
                focused={formState.focused as Focused}
                name={formState.name}
                number={formState.number}
                placeholders={{
                  name: CARDS_PLACEHOLDERS.name,
                }}
                locale={{
                  valid: CARDS_PLACEHOLDERS.validThru,
                }}
              />
            </div>
            <form
              className="tw-flex tw-flex-wrap tw-gap-y-12 tw-gap-x-16 tw-justify-center"
              onSubmit={handleSubmit(onSubmit)}
            >
              <div>
                <FormControl variant="outlined">
                  <InputLabel htmlFor="outlined-adornment-number">
                    Número
                  </InputLabel>
                  <OutlinedInput
                    id="outlined-adornment-number"
                    type="number"
                    value={formState.number}
                    {...register("number", cardValidationConfig)}
                    onChange={(event) => onChange(event, 16)}
                    label="number"
                    autoComplete="off"
                    onFocus={handleFocus}
                  />
                </FormControl>
                {errors.number && (
                  <ErrorMessage errors={errors.number as Errors} />
                )}
              </div>
              <div>
                <FormControl variant="outlined">
                  <InputLabel htmlFor="outlined-adornment-name">
                    Nombre
                  </InputLabel>
                  <OutlinedInput
                    id="outlined-adornment-name"
                    type="text"
                    value={formState.name}
                    {...register("name", nameValidationConfig)}
                    onChange={onChange}
                    label="name"
                    autoComplete="off"
                    onFocus={handleFocus}
                  />
                </FormControl>
                {errors.name && <ErrorMessage errors={errors.name as Errors} />}
              </div>
              <div>
                <FormControl variant="outlined">
                  <InputLabel htmlFor="outlined-adornment-expiry">
                    Válida hasta
                  </InputLabel>
                  <OutlinedInput
                    id="outlined-adornment-expiry"
                    type="number"
                    value={formState.expiry}
                    {...register("expiry", expirationValidationConfig)}
                    onChange={(event) => onChange(event, 4)}
                    label="expiry"
                    autoComplete="off"
                    onFocus={handleFocus}
                  />
                </FormControl>
                {errors.expiry && (
                  <ErrorMessage errors={errors.expiry as Errors} />
                )}
              </div>
              <div>
                <FormControl variant="outlined">
                  <InputLabel htmlFor="outlined-adornment-cvc">CVV</InputLabel>
                  <OutlinedInput
                    id="outlined-adornment-cvc"
                    type="number"
                    value={formState.cvc}
                    {...register("cvc", cvcValidationConfig)}
                    onChange={(event) => onChange(event, 3)}
                    label="cvc"
                    autoComplete="off"
                    onFocus={handleFocus}
                  />
                </FormControl>
              </div>
              <div className="tw-flex tw-w-full tw-justify-end tw-mt-6">
                <Button
                  type="submit"
                  className={`tw-h-12 tw-w-64 ${
                    hasErrors || !isDirty || isEmpty
                      ? "tw-text-neutral-gray-300 tw-border-neutral-gray-300 tw-cursor-not-allowed"
                      : ""
                  }`}
                  variant="outlined"
                  disabled={hasErrors || !isDirty || isEmpty}
                >
                  Agregar
                </Button>
              </div>
            </form>
          </div>
        }
      />
      {isError && (
        <SnackBar duration={duration} message={errorMessage} type="error" />
      )}
    </>
  );
}
