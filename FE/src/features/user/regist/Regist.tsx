import Input from "components/Input";
import Button from "components/Button";
import React, { useState } from "react";
import styles from "./Regist.module.css";

const Regist = (e: any) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isPassword, setIsPassword] = useState(false);
  const [isPasswordConfirm, setIsPasswordConfirm] = useState(false);

  const emailCheck = () => {
    const rep = /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    if (rep.test(email)) {
      alert("사용 가능한 이메일입니다.");
    } else {
      alert("이메일 형식이 올바르지 않습니다.");
    }
  };

  const passwordCheck = (e: any) => {
    setPassword(e.target.value);
    const rep = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{9,25}$/;
    if (rep.test(password)) {
      setIsPassword(true);
    } else {
      setIsPassword(false);
    }
  };

  const passwordConfirm = (e: any) => {
    if (e.target.value === password) {
      setIsPasswordConfirm(true);
    } else {
      setIsPasswordConfirm(false);
    }
  };

  const confirm = () => {
    console.log(isPasswordConfirm);
    console.log("really?");
  };

  return (
    <div className={styles.container}>
      <div className={styles.registContainer}>
        <div className={styles.textTitle}>SIGN UP</div>
        <div className={styles.smallText}>AI 여행 스케줄링 플래너 - MYRO</div>
        <div className={styles.inputTextContainer}>
          <label className={styles.inputText} htmlFor="name">
            이메일
          </label>
        </div>
        <div className={styles.inputEmailContainer}>
          <div style={{ width: "80%" }}>
            <Input type="text" name="email" placeholder="" onChange={(e: any) => setEmail(e.target.value)} />
          </div>
          <Button color="main" text="확인" onClick={emailCheck} />
        </div>

        <div className={styles.inputTextContainer}>
          <label className={styles.inputText} htmlFor="name">
            이름
          </label>
        </div>
        <Input type="text" name="email" placeholder="" />

        <div className={styles.inputTextContainer}>
          <label className={styles.inputText} htmlFor="name">
            닉네임
          </label>
        </div>
        <Input type="text" name="email" placeholder="" />

        <div className={styles.inputTextContainer}>
          <label className={styles.inputText} htmlFor="name">
            비밀번호
          </label>
        </div>
        <Input
          type="password"
          name="email"
          placeholder="비밀번호(문자, 숫자, 특수문자 포함 10~20자)"
          onChange={(e: any) => passwordCheck(e)}
        />

        <div className={styles.inputTextContainer}>
          <label className={styles.inputText} htmlFor="name">
            비밀번호 확인
          </label>
        </div>
        <Input type="password" name="email" placeholder="비밀번호 재입력" onChange={(e: any) => passwordConfirm(e)} />

        {/* <div style={{ display: "flex", flexDirection: "column" }}>
          <FormControlLabel
            control={<Checkbox size="small" name="age" />}
            label={<Text value="본인은 만 14세 이상입니다." type="caption" />}
          />
          <FormControlLabel
            control={<Checkbox size="small" name="age" />}
            label={<Text value="개인정보수집에 동의합니다." type="caption" />}
          />
        </div> */}

        <div style={{ margin: "20px 0 20px 0" }}>
          <div className={styles.inputCheckbox}>
            <label htmlFor="age">
              <input type="checkbox" id="age" />
              <span>본인은 만 14세 이상입니다.</span>
              <span style={{ fontSize: "8px", marginLeft: "5px", color: "#1e87f0" }}>(필수)</span>
            </label>
          </div>

          <div className={styles.inputCheckbox}>
            <label htmlFor="policy">
              <input type="checkbox" id="policy" />
              <span>개인정보수집에 동의합니다.</span>
              <span style={{ fontSize: "8px", marginLeft: "5px", color: "#1e87f0" }}>(필수)</span>
            </label>
            <a href="https://www.myro.co.kr/userPolicy" target="_blank" rel="noreferrer">
              보기
            </a>
          </div>

          <table className={styles.registTable}>
            <tbody>
              <tr>
                <td>개인정보수집</td>
              </tr>
              <tr>
                <td>목적</td>
                <td className={styles.tableSecond}>개인식별, 회원자격 유지/관리</td>
              </tr>
              <tr>
                <td>항목</td>
                <td className={styles.tableSecond}>이름, 아이디, 이메일, 닉네임, 비밀번호</td>
              </tr>
              <tr>
                <td>보유기간</td>
                <td className={styles.tableSecond}>회원탈퇴 시 즉시 파기</td>
              </tr>
            </tbody>
          </table>

          <div className={styles.inputCheckbox}>
            <label htmlFor="agreements">
              <input type="checkbox" id="agreements" />
              <span>이용약관에 동의합니다.</span>
              <span style={{ fontSize: "8px", marginLeft: "5px", color: "#1e87f0" }}>(필수)</span>
            </label>
            <a href="https://www.myro.co.kr/userAgreements" target="_blank" rel="noreferrer">
              보기
            </a>
          </div>
        </div>

        <div className={styles.registButton}>
          <div className={styles.registConfirm}>
            <Button color="main" text="회원가입" width="100%" radius onClick={confirm} />
          </div>
          <div className={styles.registBack}>
            <Button color="main" text="뒤로가기" width="100%" radius />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Regist;
