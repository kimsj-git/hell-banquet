import { useState } from "react";
import { format, addDays, startOfWeek } from "date-fns";
import { LogedPageTemplate } from "../../components/common";
import styled from "styled-components";
import { getLeftover } from "../../api/leftover";
import StatsticsToday from "../../components/analytics/StatisticsToday";
import WeekPicker from "../../components/analytics/WeekPicker";
import StatisticsWeek from "../../components/analytics/StatisticsWeek";
import StatisticWeekDou from "../../components/analytics/StatisticWeekDou";
function AnalysisPage(params) {
  const [mon, setMon] = useState([]);
  const [tue, setTue] = useState([]);
  const [wes, setWes] = useState([]);
  const [thu, setThu] = useState([]);
  const [fri, setFri] = useState([]);
  const [isToday, setIsToday] = useState(true);
  const [selectWeek, setSelectWeek] = useState([]);
  const [weekAfter, setWeekAfter] = useState(0);
  const [weekBefore, setWeekBefore] = useState(0);

  const getWeekPickerInfo = (data) => {
    const selectedDate = new Date(data.getTime());
    const endDate = addDays(selectedDate, 4);

    const startDateString = format(selectedDate, "yyyy-MM-dd");
    const endDateString = format(endDate, "yyyy-MM-dd");

    const dateRange = [];
    for (let i = 0; i < 5; i++) {
      const date = addDays(selectedDate, i);
      const dateString = format(date, "yyyy-MM-dd");
      dateRange.push(dateString);
    }

    setSelectWeek(dateRange);

    const getInfo = async () => {
      setWeekAfter(0);
      setWeekBefore(0);
      await getLeftover(
        { date: dateRange[0], userId: "string" },
        (data) => {
          setMon([data.data.before, data.data.after]);
          setWeekAfter(weekAfter + data.data.after);
          setWeekBefore(weekBefore + data.data.before);
        },
        (err) => {
          setMon([0, 0]);
        }
      );
      await getLeftover(
        { date: dateRange[1], userId: "string" },
        (data) => {
          setTue([data.data.before, data.data.after]);
          setWeekAfter(weekAfter + data.data.after);
          setWeekBefore(weekBefore + data.data.before);
        },
        (err) => {
          setTue([0, 0]);
        }
      );
      await getLeftover(
        { date: dateRange[2], userId: "string" },
        (data) => {
          setWes([data.data.before, data.data.after]);
          setWeekAfter(weekAfter + data.data.after);
          setWeekBefore(weekBefore + data.data.before);
        },
        (err) => {
          setWes([0, 0]);
        }
      );
      await getLeftover(
        { date: dateRange[3], userId: "string" },
        (data) => {
          setThu([data.data.before, data.data.after]);
          setWeekAfter(weekAfter + data.data.after);
          setWeekBefore(weekBefore + data.data.before);
        },
        (err) => {
          setThu([0, 0]);
        }
      );
      await getLeftover(
        { date: dateRange[4], userId: "string" },
        (data) => {
          setFri([data.data.before, data.data.after]);
          setWeekAfter(weekAfter + data.data.after);
          setWeekBefore(weekBefore + data.data.before);
        },
        (err) => {
          setFri([0, 0]);
        }
      );
    };
    getInfo();
  };

  if (isToday) {
    return (
      <>
        <LogedPageTemplate />
        <Container>
          <Content>
            <StatsticsToday />
          </Content>
        </Container>
      </>
    );
  } else {
    return (
      <>
        <LogedPageTemplate />
        <Container>
          <Content>
            <WeekPicker getWeekPickerInfo={getWeekPickerInfo} />
            <StatisticWeekDou weekAfter={weekAfter} weekBefore={weekBefore} />
            <StatisticsWeek mon={mon} tue={tue} wes={wes} thu={thu} fri={fri} />
          </Content>
        </Container>
      </>
    );
  }
}

const Container = styled.div`
  width: 95%;
  margin: 15px auto;
  display: flex;
  justify-content: center;
  background: #e5e5e5;
  border-radius: 30px;
`;

const Content = styled.div`
  width: 100%;
  max-width: 80vh;
  padding: 20px;
`;

export default AnalysisPage;
