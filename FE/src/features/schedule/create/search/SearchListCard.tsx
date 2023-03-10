import React, { useState } from "react";
import {
  selectHotelList,
  selectPlaceList,
  selectPointPlace,
  setHotelList,
  setPlaceList,
  setPointPlace,
} from "slices/scheduleCreateSlice";
import { useAppDispatch, useAppSelector } from "app/hooks";
import styles from "./Search.module.css";
import { Info, Add } from "@mui/icons-material";
import { IconButton } from "@mui/material";
import Modal from "@mui/material/Modal";
import Text from "components/Text";
import SearchCardInfoModal from "./SearchCardInfoModal";
import { ScheduleCreatPropsType } from "pages/ScheduleCreatePage";

interface SearchListCardType {
  cardInfo: { id: number; image: string; name: string };
  select: string;
  scheduleCreatProps: ScheduleCreatPropsType;
}

function SearchListCard({ cardInfo, select, scheduleCreatProps }: SearchListCardType) {
  const dispatch = useAppDispatch();
  const hotel = useAppSelector(selectHotelList);
  const place = useAppSelector(selectPlaceList);
  const pointPlace = useAppSelector(selectPointPlace);
  const [ModalOpen, setModalOpen] = useState(false);
  const { hotelCurrentDay, placeCurrentDay, setCurrentTab, setHotelCurrentDay, setPlaceCurrentDay } =
    scheduleCreatProps;

  const InfoAddClick = () => {
    setCurrentTab(select);
    if (select === "νΈν") {
      const hotelList = [...hotel];
      hotelList[hotelCurrentDay] = cardInfo;
      setHotelCurrentDay(hotelCurrentDay + 1 == hotel.length ? 0 : hotelCurrentDay + 1);
      dispatch(setHotelList([...hotelList]));
    }
    if (select === "μ₯μ") {
      if (placeCurrentDay != -1) {
        setPlaceCurrentDay(-1);
        const pointPlaceList = [...pointPlace];
        pointPlaceList[placeCurrentDay] = cardInfo;
        dispatch(setPointPlace([...pointPlaceList]));
      } else {
        const placeList = [...place];
        placeList.push({
          id: cardInfo.id,
          image: cardInfo.image,
          name: cardInfo.name,
          time: "2:00",
        });
        dispatch(setPlaceList([...placeList]));
      }
    }
  };

  return (
    <div className={styles.card}>
      <img src={cardInfo.image} alt="" />
      <div className={styles.placeCard}>
        <Text value={cardInfo.name} type={"caption"} />
        <div className={styles.card_Icons}>
          <div />
          <div />
          <IconButton disableRipple sx={{ padding: "0px" }} onClick={() => setModalOpen(true)}>
            <Info fontSize="small" className={styles.info_icon} />
          </IconButton>
          <IconButton size="small" disableRipple onClick={() => InfoAddClick()}>
            <Add fontSize="small" className={styles.add_icon} />
          </IconButton>
        </div>
      </div>
      {ModalOpen && (
        <Modal open={ModalOpen} onClose={() => setModalOpen(false)}>
          <SearchCardInfoModal
            place={cardInfo}
            setModalOpen={() => setModalOpen(false)}
            InfoAddClick={() => InfoAddClick()}
          />
        </Modal>
      )}
    </div>
  );
}

export default SearchListCard;
