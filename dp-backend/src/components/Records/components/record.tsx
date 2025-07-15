import React from "react";
import {
  formatCurrency,
  formatDateFromString,
  isVisa,
  isMastercard,
  deleteUserCard,
  calculateTransacionType,
  getAccount,
} from "../../../utils/";
import {
  currencies,
  ROUTES,
  ID,
  SUCCESS,
  MESSAGE,
  SUCCESS_MESSAGES_KEYS,
  RECORD_MESSAGES,
  STEP,
  DESTINATION,
  UNAUTHORIZED,
} from "../../../constants/";
import { useSearchParams, useNavigate, Link } from "react-router-dom";
import { Icon, IconType } from "./../../Icon";
import { Transaction, Card, Account, ActivityType } from "../../../types";
import { useAuth, useLocalStorage, useUserInfo } from "../../../hooks";

export enum RecordVariant {
  TRANSACTION = "transaction",
  CARD = "card",
  ACCOUNT = "account",
}

export interface IRecord {
  content: Transaction | Card | Account;
  variant?: RecordVariant;
}

export interface RecordProps extends IRecord {
  className?: string;
  setRecords?: React.Dispatch<React.SetStateAction<IRecord[]>>;
  isSelecting?: boolean;
  onSelect?: (id: string) => void;
}
const { Argentina } = currencies;
const { locales, currency } = Argentina;

const iconType: Record<ActivityType, IconType> = {
  [ActivityType.TRANSFER_IN]: "TransferIN",
  [ActivityType.TRANSFER_OUT]: "TransferOUT",
  [ActivityType.DEPOSIT]: "Deposit",
};

export const Record = ({
  className,
  content,
  variant = RecordVariant.TRANSACTION,
  setRecords,
  isSelecting = false,
  onSelect,
}: RecordProps) => {
  return (
    <li
      className={`tw-flex tw-w-full tw-justify-between tw-px-4 tw-border-t tw-border-neutral-blue-100 tw-py-5 hover:tw-bg-neutral-gray-500 tw-transition ${className}`}
    >
      {variant === RecordVariant.TRANSACTION && (
        <TransactionItem {...(content as Transaction)} />
      )}
      {variant === RecordVariant.CARD && (
        <CardItem
          {...(content as Card)}
          setRecords={setRecords}
          isSelecting={isSelecting}
          onSelect={onSelect}
        />
      )}
      {variant === RecordVariant.ACCOUNT && (
        <AccountItem {...(content as Account)} />
      )}
    </li>
  );
};

function TransactionItem({
  amount,
  origin,
  destination,
  realization_date: dated,
  id,
  transaction_type: type,
}: Transaction) {
  const calculatedType = calculateTransacionType(amount, type);

  return (
    <Link
      className="tw-w-full tw-flex tw-justify-between tw-items-center"
      to={`${ROUTES.ACTIVITY_DETAILS}?${ID}${id}`}
    >
      <div className="tw-flex tw-items-center tw-gap-x-4">
        {calculatedType && (
          <Icon
            className="tw-fill-neutral-gray-500"
            type={iconType[calculatedType]}
          />
        )}
        <p>
          {RECORD_MESSAGES[calculatedType] && RECORD_MESSAGES[calculatedType]}{" "}
          {calculatedType === ActivityType.TRANSFER_OUT
            ? destination
            : calculatedType === ActivityType.TRANSFER_IN
            ? origin
            : null}
        </p>
      </div>
      <div className="tw-flex tw-text-left tw-flex-col tw-items-end">
        <p>{formatCurrency(locales, currency, amount)}</p>
        <p>{formatDateFromString(dated)}</p>
      </div>
    </Link>
  );
}

function CardItem({
  number,
  card_type,
  isSelecting,
  id: cardId,
  setRecords,
  onSelect,
}: Card & {
  setRecords?: React.Dispatch<React.SetStateAction<IRecord[]>>;
  isSelecting: boolean;
  onSelect?: (id: string) => void;
}) {
  const navigate = useNavigate();

  const lastFourDigits = (number && number.toString().slice(-4)) || "";
  const isVisaCard = isVisa(number);
  const isMasterCard = isMastercard(number);
  const type = isVisaCard
    ? "visa"
    : isMasterCard
    ? "mastercard"
    : "credit-card";
  const { user } = useUserInfo();
  const { logout } = useAuth();
  const [token] = useLocalStorage("token");

  const handleDelete = () => {
    if (user && user.id) {
      getAccount(user.id, token).then((account) => {
        deleteUserCard(account.id, cardId, token)
          .then((response) => {
            if (response.status === UNAUTHORIZED) {
              logout();
            }
            if (setRecords) {
              setRecords((prev) =>
                prev.filter((record) => (record.content as Card).id !== cardId)
              );
            }
            navigate(
              `${ROUTES.CARDS}?${SUCCESS}&${MESSAGE}${SUCCESS_MESSAGES_KEYS.CARD_DELETED}`
            );
          })
          .catch((error) => {
            if (error.status === UNAUTHORIZED) {
              logout();
            }
          });
      });
    }
  };

  return (
    <>
      <div className="tw-flex tw-items-center tw-gap-x-4">
        <Icon type={type} />

        <p>
          {type} terminada en {lastFourDigits}
        </p>
      </div>
      <div className="tw-flex tw-text-left tw-gap-x-4 tw-items-center">
        {isSelecting ? (
          <button
            onClick={() => onSelect && onSelect(cardId)}
            className="tw-text-primary"
          >
            Seleccionar
          </button>
        ) : (
          <button className="tw-text-error" onClick={handleDelete}>
            Eliminar
          </button>
        )}
      </div>
    </>
  );
}

function AccountItem({ name, origin }: Account) {
  const navigate = useNavigate();

  return (
    <>
      <div className="tw-flex tw-items-center tw-gap-x-4">
        <Icon type="user" />
        <p>{name}</p>
      </div>
      <div className="tw-flex tw-text-primary tw-text-left tw-gap-x-4 tw-items-center">
        <button
          onClick={() =>
            navigate(`${ROUTES.SEND_MONEY}?${STEP}2&${DESTINATION}${origin}`)
          }
        >
          Seleccionar
        </button>
      </div>
    </>
  );
}
