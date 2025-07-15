export * from './formValidation';
export * from './currencyFormatter';
export * from './date';
export * from './copy';
export * from './numberCard';
export * from './print';
export * from './api';
export * from './parseData';
export * from './pagination';
export * from './transaction';
export * from './sort';

export const isCVU = (value: string): boolean => {
  return /^\d{22}$/.test(value);
};