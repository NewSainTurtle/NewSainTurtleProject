import React from "react";
import Input from "components/Input";
import Text from "components/Text";
import Button from "components/Button";
import styles from "./MyProfile.module.css";
import loginStyles from "../../login/Login.module.css";

const MyProfileEdit = () => {
  return (
    <div className={styles.profileWrapper}>
      <div className={styles.profileContainer}>
        <div className={styles.profileTopContainer}>
          <div className={styles.profileImgContainer}>
            <div className={styles.profileImgText}>김</div>
          </div>
          <div className={styles.profileUser}>
            <Text value="김연수" type="pageTitle" bold />
          </div>
          <div className={styles.text}>님의 프로필</div>
        </div>
        <div className={styles.profileBotContainer}>
          <div className={styles.profileInfoContainer}>
            <div className={styles.profileInfoTitleContainer}>
              <div className={styles.profileInfoTitle}>
                <Text value="기본정보" type="text" bold />
              </div>
            </div>

            <div className={loginStyles.inputTextContainer}>
              <label className={loginStyles.inputText} style={{ color: "black" }} htmlFor="name">
                이름
              </label>
            </div>
            <Input type="text" name="email" placeholder="" />

            <div className={loginStyles.inputTextContainer}>
              <label className={loginStyles.inputText} style={{ color: "black" }} htmlFor="name">
                닉네임
              </label>
            </div>
            <Input type="text" name="email" placeholder="" />

            <div className={loginStyles.inputTextContainer}>
              <label className={loginStyles.inputText} style={{ color: "black" }} htmlFor="name">
                이메일
              </label>
            </div>
            <Input type="text" name="email" placeholder="" disabled />
          </div>
        </div>
        <div className={styles.profileLeave}>회원탈퇴</div>

        <div className={styles.profileBtnContainer}>
          <div className={styles.profileBtn}>
            <Button width="100%" height="100%" text="취소하기" />
          </div>
          <div className={styles.profileBtn}>
            <Button width="100%" height="100%" text="저장하기" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyProfileEdit;
