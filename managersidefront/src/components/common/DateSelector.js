import { TextField } from "@mui/material";
import { useEffect, useState } from "react";

function DateSelector(params) {
  const today = new Date().toISOString().split("T")[0];
  const { setDate } = params;
  const [targetDate, setTargetDate] = useState(today);

  useEffect(() => {
    if (!params?.date) {
      setTargetDate(new Date());
    }
  }, [params?.date]);

  const handleDateChange = (e) => {
    const newDate = new Date(e.target.value).toISOString().split("T")[0];
    setTargetDate(newDate);
    setDate(newDate);
  };

  return (
    <TextField type='date' value={targetDate} onChange={handleDateChange} />
  );
}

export default DateSelector;
