import { ActivityType } from "../../types";

export const calculateTransacionType = (
  amount: number,
  type: string
): ActivityType => {
  return type as ActivityType;
};
