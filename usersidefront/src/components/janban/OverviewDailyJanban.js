import { useState, useEffect } from "react";

import staticJanban from "../../assets/images/janban.png";
import { LinkDecoNone } from "../common";
import { getJanbanImg } from "../../api/janbani";

import styled from "styled-components";
import { Button, Container } from "@mui/material";

function OverviewDailyJanban() {
  const [janbanImg, setJanbanImg] = useState(staticJanban);
  const date = new Date().getHours();
  const janbanCode = "GRD_002";
  const janbanOption =
    date < 14
      ? { url: `/record-meal`, message: "식사하러 가기" }
      : { url: `/janban`, message: "잔반이 확인하기" };

  useEffect(() => {
    const handleGetJanban = async () => {
      await getJanbanImg(
        { janbanCode: janbanCode },
        (data) => {
          const blob = new Blob([data.data], { type: "image/png" });
          const imageURL = URL.createObjectURL(blob);
          return imageURL;
        },
        (err) => console.log(err)
      ).then((res) => {
        setJanbanImg(res);
      });
    };
    handleGetJanban();
  }, []);

  useEffect(() => {
    console.log(janbanImg);
    console.log(JanbanImg);
  }, [janbanImg]);

  return (
    <OverviewBox>
      <img src={"http://localhost:3000/f87a806b-055f-4b91-a073-4f8c2ab20e33"} />
      <JanbanImg src={janbanImg} alt='잔반이' />
      <Container style={{ position: "relative", height: "100%" }}>
        <LinkDecoNone
          to={janbanOption.url}
          style={{ position: "absolute", bottom: "10%", right: "15%" }}
        >
          <Button variant='contained' color='warning'>
            {janbanOption.message}
          </Button>
        </LinkDecoNone>
      </Container>
    </OverviewBox>
  );
}

const OverviewBox = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const JanbanImg = styled.img`
  width: auto;
  height: auto;
  max-width: 200px;
  max-height: 200px;
`;

export default OverviewDailyJanban;
