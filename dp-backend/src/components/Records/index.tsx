import React from 'react';
import { RecordProps, Record, IRecord } from './components/record';

export interface RecordsProps {
  records: RecordProps[];
  maxRecords?: number;
  initialRecord?: number;
  setRecords?:React.Dispatch<React.SetStateAction<IRecord[]>>;
  isSelecting?: boolean;
  onSelect?: (id: string) => void;
}

export const Records = ({
  records,
  maxRecords,
  initialRecord = 0,
  setRecords,
  isSelecting = false,
  onSelect
}: RecordsProps) => {
  const recordsToShow = records.slice(initialRecord, maxRecords);
  return (
    <ul className="tw-w-full">
      {recordsToShow &&
        recordsToShow.map((record: RecordProps, index: number) => (
          <Record
            key={`record-${index}`}
            {...record}
            className={`
              ${index + 1 === recordsToShow.length && 'tw-border-b'}`}
              setRecords={setRecords}
              isSelecting={isSelecting}
              onSelect={onSelect}
          />
        ))}
    </ul>
  );
};

export * from './components/record';
