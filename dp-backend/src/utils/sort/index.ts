import { Transaction } from "../../types";

export const sortByDate = (
  transactions: Transaction[],
  mode = "desc"
): Transaction[] => {
  if (mode === "desc" && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return (
        new Date(b.realization_date).getTime() -
        new Date(a.realization_date).getTime()
      );
    });
  }
  if (mode === "asc" && transactions.length > 0) {
    return transactions.sort((a, b) => {
      return (
        new Date(a.realization_date).getTime() -
        new Date(b.realization_date).getTime()
      );
    });
  }

  return transactions;
};
