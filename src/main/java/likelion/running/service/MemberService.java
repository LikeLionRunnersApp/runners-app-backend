package likelion.running.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.running.domain.Result.KakaoResult;
import likelion.running.domain.member.*;
import likelion.running.domain.Result.SignUpResult;
import likelion.running.domain.token.KakaoToken;
import likelion.running.web.dto.memberDto.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;//멤버 저장소
    private final MemberAuthorityJpaRepository memberAuthorityJpaRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final String PHONE_PATTERN = "^(02|0[1-9][0-9]?)-[0-9]{3,4}-[0-9]{4}$";
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);

    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository, MemberAuthorityJpaRepository memberAuthorityJpaRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberAuthorityJpaRepository = memberAuthorityJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public SignUpResult signUp(SignUpDto memberDto){
        if(!isValidEmail(memberDto.getMemberId())){
            return new SignUpResult("idValidation");
        }
        if(memberJpaRepository.findOneWithAuthoritiesByMemberId(memberDto.getMemberId()).orElse(null)!=null){
            return new SignUpResult("duplicatedId");
        }
        if(!memberDto.getPassword().equals(memberDto.getCheckPassWord())){
            return new SignUpResult("pwValidation");
        }
        if(!isValidPhone(memberDto.getPhoneNum())){
            log.info(memberDto.getPhoneNum());
            return new SignUpResult("WrongNum");
        }

        Member member = Member.builder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .phoneNum(memberDto.getPhoneNum())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .authorities(new HashSet<>())
                .activated(true)
                .build();

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .member(member)
                .authority(authority)
                .build();

        member.getAuthorities().add(memberAuthority);

        log.info(member.getMemberId());
        memberAuthorityJpaRepository.save(memberAuthority);
        Member save = memberJpaRepository.save(member);
        log.info("멤버 저장 됨 {}",save.getId());
        log.info(save.getPassword());

        return new SignUpResult("true");
    }

    public Optional<Member> findById(Long id){
        return memberJpaRepository.findMemberById(id);
    }

    public Optional<Member> findByName(String name){
        return memberJpaRepository.findMemberByName(name);
    }
    public Optional<Member> findByMemberId(String memberId){
        return memberJpaRepository.findMemberByMemberId(memberId);
    }
    public Optional<Member> findByNameAndPhoneNum(MemberDto memberDto){
        return memberJpaRepository.findMemberByNameAndPhoneNum(memberDto.getName(),memberDto.getPhoneNum());
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhone(String phone){
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }

    @Transactional
    public boolean checkAuthCode(String memberId, String authCode){
        log.info("MemberId = {}",memberId);
        log.info("authCode = {}",authCode);
        Optional<Member> member = memberJpaRepository.findMemberByMemberId(memberId);
        log.info(String.valueOf(member.isEmpty()));
        return member.map(value -> value.getAuthCode().equals(authCode)).orElse(false);
    }

    @Transactional
    public boolean authCodeEdit(Member member, String message){

        Optional<Member> value = memberJpaRepository.findMemberByMemberId(member.getMemberId());

        if(value.isPresent()){
            MemberEditDto build = value.get().toEditor().AuthCode(message).build();
            value.get().edit(build);
            log.info("인증 코드 갱신 성공");
            return true;
        }
        log.info("인증 코드 갱신 실패");
        return false;
    }

    @Transactional
    public ResponseEntity<TokenDto> kakaoSignUp(KakaoSignUpDto kakaoSignUpDto){

        String accessToken = kakaoSignUpDto.getAccessToken();
        KakaoResult kakaoResult = requestUser(accessToken);

        Member member = Member.builder()
                .memberId(kakaoResult.getEmail())
                .name(kakaoSignUpDto.getName())
                .phoneNum(kakaoSignUpDto.getPhoneNum())
                .authorities(new HashSet<>())
                .activated(true)
                .build();

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        MemberAuthority memberAuthority = MemberAuthority.builder()
                .member(member)
                .authority(authority)
                .build();

        member.getAuthorities().add(memberAuthority);

        log.info(member.getMemberId());
        memberAuthorityJpaRepository.save(memberAuthority);
        Member save = memberJpaRepository.save(member);
        log.info("멤버 저장 됨 {}",save.getId());
        log.info(save.getPassword());

        LoginDto loginDto = new LoginDto();
        loginDto.setMemberId(member.getMemberId());
        return tokenService.makeTokenWithKakao(loginDto.getMemberId());
    }

    //인증코드로 token요청하기
    public KakaoToken requestToken(String code) {

        String strUrl = "https://kauth.kakao.com/oauth/token"; //request를 보낼 주소
        KakaoToken kakaoToken = new KakaoToken(); //response를 받을 객체

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url Http 연결 생성

            //POST 요청
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//outputStreamm으로 post 데이터를 넘김

            //파라미터 세팅
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            //0번 파라미터 grant_type 입니다 authorization_code로 고정이라니 고정등록해줍니다
            sb.append("grant_type=authorization_code");

            //1번 파라미터 client_id입니다. ***자신의 앱 REST API KEY로 변경해주세요***
            sb.append("&client_id=ebfe916f118c6750385fc7e0a5b92b09");

            //2번 파라미터 redirect_uri입니다. ***자신의 redirect uri로 변경해주세요***
            sb.append("&redirect_uri=http://localhost:8080/kakaologin/redirect");

            //3번 파라미터 code입니다. 인자로 받아온 인증코드입니다.
            sb.append("&code=" + code);

            sb.append("&client_secret=UT83VPhLnTnI9qK25fRGkgSGmfOZDfyN");

            bw.write(sb.toString());
            bw.flush();//실제 요청을 보내는 부분

            //실제 요청을 보내는 부분, 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responsecode(200이면성공): {}", responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            log.info("response body: {}", result);


            //Jackson으로 json 파싱할 것임
            ObjectMapper mapper = new ObjectMapper();
            //kakaoToken에 result를 KakaoToken.class 형식으로 변환하여 저장
            kakaoToken = mapper.readValue(result, KakaoToken.class);

            //api호출용 access token
            String access_Token = kakaoToken.getAccess_token();
            //access 토큰 만료되면 refresh token사용(유효기간 더 김)
            String refresh_Token =kakaoToken.getRefresh_token();


            log.info("access_token = {}", access_Token);
            log.info("refresh_token = {}", refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("카카오토큰생성완료>>>{}", kakaoToken);
        return kakaoToken;
    }

    public KakaoResult requestUser(String accessToken){
        log.info("requestUser 시작");
        String strUrl = "https://kapi.kakao.com/v2/user/me"; //request를 보낼 주소
        KakaoResult user = new KakaoResult(); //response를 받을 객체

        try{
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url Http 연결 생성

            //POST 요청
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//outputStreamm으로 post 데이터를 넘김

            //전송할 header 작성, 인자로 받은 access_token전송
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);


            //실제 요청을 보내는 부분, 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("requestUser의 responsecode(200이면성공): {}",responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();

            log.info("response body: {}",result);


            //Jackson으로 json 파싱할 것임
            ObjectMapper mapper = new ObjectMapper();
            //결과 json을 HashMap 형태로 변환하여 resultMap에 담음
            HashMap<String,Object> resultMap = mapper.readValue(result, HashMap.class);
            //json 파싱하여 id 가져오기
            Long id = Long.valueOf(String.valueOf(resultMap.get("id")));

            //결과json 안에 properties key는 json Object를 value로 가짐
            HashMap<String,Object> properties = (HashMap<String, Object>) resultMap.get("properties");
            String nickname = (String)properties.get("nickname");

            //결과json 안에 kakao_account key는 json Object를 value로 가짐
            HashMap<String,Object> kakao_account = (HashMap<String, Object>) resultMap.get("kakao_account");
            String email=null;//이메일은 동의해야 알 수 있음
            if(kakao_account.containsKey("email")){//동의하면 email이 kakao_account에 key로 존재함
                email=(String)kakao_account.get("email");
            }

            //유저정보 세팅
            user.setEmail(email);
            user.setId(id);
            user.setNickname(nickname);

            log.info("resultMap= {}",resultMap);
            log.info("properties= {}",properties);


        }catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}