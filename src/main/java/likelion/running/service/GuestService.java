package likelion.running.service;

import likelion.running.domain.guest.Guest;
import likelion.running.domain.guest.GuestJpaRepository;
import likelion.running.web.dto.memberDto.GuestDto;
import likelion.running.web.dto.memberDto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class GuestService {

    private final GuestJpaRepository guestJpaRepository;
    private final BoardService boardService;
    @Autowired
    public GuestService(GuestJpaRepository guestJpaRepository, BoardService boardService) {
        this.guestJpaRepository = guestJpaRepository;
        this.boardService = boardService;
    }

    public Guest joinRunning(GuestDto guestDto){
        return guestJpaRepository.save(Guest.builder()
                .guestId(guestDto.getMemberId())
                .boardId(guestDto.getBoardId())
                .participate(guestDto.isParticipate())
                .build());
    }
    public List<Guest> findMembers(MemberDto memberDto){
        log.info(memberDto.toString());
        Long boardId = boardService.findByHostId(memberDto.getHostId());
        log.info("boardId = {}",boardId);
        return guestJpaRepository.findAllByBoardId(boardId);
    }
}
