import React, { useMemo } from "react";
import { useAppDispatch, useAppSelector } from "app/hooks";
import {
  selectPlaceList,
  selectPointPlace,
  setPlaceList,
  setPlaceTime,
  setPointPlace,
} from "slices/scheduleCreateSlice";
import { Box, Divider } from "@mui/material";
import styles from "./Info.module.css";
import Text from "components/Text";
import { Close, Timer } from "@mui/icons-material";
import { Stack } from "@mui/system";
import { ScheduleCreatPropsType } from "pages/ScheduleCreatePage";

function InfoListPlace(props: { scheduleCreatProps: ScheduleCreatPropsType }) {
  const dispatch = useAppDispatch();
  const place = useAppSelector(selectPlaceList);
  const pointPlace = useAppSelector(selectPointPlace);
  const timer = useMemo(() => {
    return place.map((val: { time: string }) => {
      const time = val.time.split(":");
      return {
        hour: Number(time[0]),
        minute: Number(time[1]),
      };
    });
  }, [place]);
  const totalTime = useMemo(() => {
    const hourArr = place.map((val) => val?.time.split(":")[0]).map((i) => Number(i));
    const minuteArr = place.map((val) => val?.time.split(":")[1]).map((i) => Number(i));
    const sumHour = hourArr.reduce((accumulator, current) => accumulator + current, 0);
    const sumMinute = minuteArr.reduce((accumulator, current) => accumulator + current, 0);
    return {
      hour: sumHour,
      minute: sumMinute,
    };
  }, [place, timer]);
  const { placeCurrentDay, setPlaceCurrentDay } = props.scheduleCreatProps;

  const deletePlace = (id: number) => {
    const newPlaceList = place.filter((value) => value.id !== id);
    dispatch(setPlaceList(newPlaceList));
  };

  const deletePlaceAll = () => {
    dispatch(setPointPlace(Array.from({ length: 2 }, () => null)));
    dispatch(setPlaceList([]));
  };

  const deletePointPlace = (index: number) => {
    const pointPlaceList = [...pointPlace];
    pointPlaceList[index] = null;
    dispatch(setPointPlace([...pointPlaceList]));
  };

  const onChangeAccount = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, name } = e.target;
    const newTime = [...timer];
    if (name === "hour") {
      if (parseInt(value) > 24) {
        newTime[index].hour = 23;
      } else newTime[index].hour = parseInt(value.replace(/^([1-9]|[01][0-9]|2[0-3])$/, value));
    } else if (name === "minute") {
      if (parseInt(value) > 60) {
        newTime[index].minute = 59;
      } else newTime[index].minute = parseInt(value.replace(/^([0-5][0-9])$/, value));
    }
    const timeToString = timer[index].hour + ":" + timer[index].minute;
    dispatch(setPlaceTime({ index: index, time: timeToString === "0:0" ? "0:1" : timeToString }));
  };

  const onBurCheck = (index: number, e: React.FocusEvent<HTMLInputElement, Element>) => {
    const { value, name } = e.target;
    if (value == "") {
      const newTime = [...timer];
      if (name === "hour") {
        newTime[index].hour = 1;
      } else if (name === "minute") {
        newTime[index].minute = 30;
      }
      const timeToString = timer[index].hour + ":" + timer[index].minute;
      dispatch(setPlaceTime({ index: index, time: timeToString === "0:0" ? "0:1" : timeToString }));
    }
  };

  return (
    <>
      <div className={styles.flex}>
        <Box my={2}>
          <Text
            value={place.filter((element) => null != element).length.toString()}
            type="textTitle"
            color="yellow"
            en
          />
          <span>
            {" "}
            (???{totalTime.hour}??????{totalTime.minute}???)
          </span>
        </Box>

        <button className={`${styles.btn} ${styles.delete_btn}`} onClick={deletePlaceAll}>
          ??????????????????
        </button>
        <Stack className={styles.flex} my={1} spacing={0.5}>
          <Divider className={styles.divider} />
          {pointPlace.map((pointPlaceCard, index) => (
            <>
              <div className={` ${styles.flexRow} ${styles.placeDrop}`}>
                <small>{`${index == 0 ? "??????" : "??????"} ??????`}</small>
              </div>
              {pointPlaceCard === null ? (
                <>
                  <div
                    key={index}
                    className={
                      placeCurrentDay === index
                        ? `${styles.place_explain} ${styles.explain_focused}`
                        : `${styles.place_explain}`
                    }
                    onClick={() => {
                      placeCurrentDay === index ? setPlaceCurrentDay(-1) : setPlaceCurrentDay(index);
                    }}
                  >
                    <p>{`??? ?????? ????????? ?????? ${index == 0 ? "?????????" : "?????????"} ????????? ???????????????`}</p>
                  </div>
                </>
              ) : (
                <div className={styles.cardList}>
                  <div className={styles.card}>
                    <img src={pointPlaceCard.image} alt={""} />
                    <div className={styles.placeCard}>
                      <span className={styles.cardText}>{pointPlaceCard.name}</span>
                      <div className={styles.flexRow}>
                        <div className={styles.placeTimer} />
                        <button className={styles.cardDelete} onClick={() => deletePointPlace(index)}>
                          <Close fontSize="small" color="error" />
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </>
          ))}
        </Stack>
        <Divider className={styles.divider} />
        {place.length > 0 ? (
          <>
            {place.map((placeCard, index) => (
              <div className={styles.cardList} key={index}>
                <div className={styles.card}>
                  <img src={placeCard?.image} alt={""} />
                  <div className={styles.placeCard}>
                    <span className={styles.cardText}>{placeCard?.name}</span>
                    <div className={styles.flexRow}>
                      <div className={styles.placeTimer}>
                        <Timer fontSize="small" />
                        <input
                          name="hour"
                          value={timer[index].hour}
                          id="placeTime"
                          type="number"
                          dir="rtl"
                          min="0"
                          max="23"
                          onBlur={(e) => onBurCheck(index, e)}
                          onChange={(e) => onChangeAccount(index, e)}
                        />
                        ??????
                        <input
                          name="minute"
                          value={timer[index].minute}
                          id="placeTime"
                          type="number"
                          dir="rtl"
                          min="0"
                          max="59"
                          onBlur={(e) => onBurCheck(index, e)}
                          onChange={(e) => onChangeAccount(index, e)}
                        />
                        ???
                      </div>
                      <button className={styles.cardDelete} onClick={() => deletePlace(placeCard?.id)}>
                        <Close fontSize="small" color="error" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </>
        ) : (
          <>
            <Box my={0.5}>
              <p className={styles.explain}>?????? ?????? ???????????? ???????????? ??????????????????.</p>
              <p className={styles.explain}>???????????? ????????? ???????????? ?????????</p>
              <p className={styles.explain}>?????? ?????? ?????? 8??? ???????????? ?????? ???????????????.</p>
            </Box>
          </>
        )}
      </div>
    </>
  );
}

export default InfoListPlace;
