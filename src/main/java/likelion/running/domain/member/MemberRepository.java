package likelion.running.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long,Member> store = new ConcurrentHashMap<>();//멤버 저장소
    private static final MemberRepository instance = new MemberRepository();//외부 접근 객체
    private static Long sequence = 0L;//멤버 아이디 자동할당
    public static MemberRepository getInstance(){
        return instance;
    }
    private MemberRepository(){
    }
    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(),member);
        return member;
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Member> findByName(String name){
        List<Member> members = findAll();
        for (Member member : members) {
            if(member.getName().equals(name)){
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
