export enum TransactionType {
  TransferIN = "TransferIN",
  TransferOUT = "TransferOUT",
  Deposit = "Deposit",
}
export interface Transaction {
  amount: number;
  name?: string;
  realization_date: string;
  id: string;
  transaction_type: TransactionType;
  origin?: string;
  destination?: string;
}

export interface Card {
  number: string;
  holder: string;
  cvv: number;
  account_id: string;
  bank: string;
  expiration_date: string;
  card_type: string;
  id: string;
}

export interface Account {
  name: string;
  origin: string;
}

export enum ActivityType {
  TRANSFER_IN = "TransferIN",
  TRANSFER_OUT = "TransferOUT",
  DEPOSIT = "Deposit",
}
