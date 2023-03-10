import { createSlice } from "@reduxjs/toolkit";
import { rootState } from "app/store";

interface placeInfoConfig {
  id: number;
  img: string;
  placeName: string;
  time: string;
  startTime: string;
  endTime: string;
}

export interface fullScheduleListConfig {
  day: number;
  dayList: placeInfoConfig[];
}

interface scheduleEditConfig {
  fullScheduleList: fullScheduleListConfig[];
  keepPlaceList: placeInfoConfig[];
}

const initialState: scheduleEditConfig = {
  fullScheduleList: [
    {
      day: 1,
      dayList: [
        {
          id: 1,
          img: "src",
          placeName: "제주공항",
          time: "1시간 0분",
          startTime: "10:00",
          endTime: "11:00",
        },
        {
          id: 2,
          img: "src",
          placeName: "성산 일출봉",
          time: "2시간 0분",
          startTime: "13:00",
          endTime: "15:00",
        },
        {
          id: 3,
          img: "src",
          placeName: "우도",
          time: "2시간 0분",
          startTime: "16:00",
          endTime: "18:00",
        },
      ],
    },
    {
      day: 2,
      dayList: [
        {
          id: 4,
          img: "src",
          placeName: "협재 해수욕장",
          time: "1시간 0분",
          startTime: "10:00",
          endTime: "11:00",
        },
        {
          id: 5,
          img: "src",
          placeName: "도두 무지개 해안도로",
          time: "1시간 0분",
          startTime: "13:00",
          endTime: "14:00",
        },
      ],
    },
    {
      day: 3,
      dayList: [
        {
          id: 6,
          img: "src",
          placeName: "쇠소깍",
          time: "2시간 0분",
          startTime: "10:00",
          endTime: "12:00",
        },
        {
          id: 7,
          img: "src",
          placeName: "카멜리아 힐",
          time: "1시간 0분",
          startTime: "14:00",
          endTime: "15:00",
        },
      ],
    },
  ],
  keepPlaceList: [
    {
      id: 8,
      img: "src",
      placeName: "월정리 해수욕장",
      time: "2시간 0분",
      startTime: "10:00",
      endTime: "12:00",
    },
  ],
};

const scheduleEditSlice = createSlice({
  name: "scheduleEdit",
  initialState,
  reducers: {
    setFullScheduleList: (state, { payload }) => {
      state.fullScheduleList = payload;
    },
    setKeepPlaceList: (state, { payload }) => {
      state.keepPlaceList = payload;
    },
  },
});

export const { setFullScheduleList, setKeepPlaceList } = scheduleEditSlice.actions;

export const selectFullScheduleList = (state: rootState) => state.scheduleEdit.fullScheduleList;
export const selectKeepPlaceList = (state: rootState) => state.scheduleEdit.keepPlaceList;

export default scheduleEditSlice.reducer;
