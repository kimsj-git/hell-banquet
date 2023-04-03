import { useEffect } from "react";
import styled from "styled-components";
import { getAnalysis } from "../../api/leftover";
import { useState } from "react";

function DailyStatistics(params) {
  const { date, course } = params;
  const [info, setInfo] = useState({
    id: 1,
    served: 1062,
    leftovers: 59,
    courseNo: 1,
    date: "2023-03-24",
  });

  useEffect(() => {
    getAnalysis(
      { startDate: date, endDate: date },
      (data) => {
        // console.log(data);
        return data.data;
      },
      (err) => console.log(err)
    ).then((data) => setInfo(data));
  });
  return (
    <CicleStaticContainer>{JSON.stringify(info[course])}</CicleStaticContainer>
  );
}

const CicleStaticContainer = styled.div`
  height: 30vh;
  width: 100%;
  // background: rgba(103, 80, 164, 0.2);
  margin: 4% 0 4% 0;
`;

export default DailyStatistics;
