import React from "react";
import styles from "../features/main/Main.module.css";
import MainDestinations from "../features/main/MainDestinations";
import MainDestinationsList from "../features/main/MainDestinationsList";
import MainDisplay from "../features/main/MainDisplay";
import MainTravelLog from "../features/main/MainTravelLog";

function MainPage() {
  return (
    <div>
      <MainDisplay />
      <div className={styles.container}>
        <div className={styles.containerInner}>
          <MainTravelLog />
          <MainDestinations />
          <MainDestinationsList />
        </div>
      </div>
    </div>
  );
}

export default MainPage;
