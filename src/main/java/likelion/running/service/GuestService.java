package likelion.running.service;

import likelion.running.domain.board.Board;
import likelion.running.domain.guest.Guest;
import likelion.running.domain.guest.GuestJpaRepository;
import likelion.running.domain.participate.ParticipateResult;
import likelion.running.web.dto.memberDto.GuestDto;
import likelion.running.web.dto.memberDto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public ParticipateResult joinRunning(GuestDto guestDto){

        Optional<Board> board = boardService.findByBoardId(guestDto.getBoardId());

        if(board.isPresent()){
            int joinMember = board.get().getJoinMember();
            if(joinMember>=board.get().getTotalMember()){
                return ParticipateResult.FULL;
            }
            else {
                Guest build = Guest.builder()
                        .board(board.get())
                        .guestId(guestDto.getMemberId())
                        .participate(guestDto.isParticipate())
                        .build();
                guestJpaRepository.save(build);
                board.get().joinGuest(build);

                boardService.increaseMember(guestDto);
                return ParticipateResult.TRUE;
            }
        }
        else {
            return ParticipateResult.FALSE;
        }
    }
    public List<Guest> findMembers(MemberDto memberDto){
        log.info(memberDto.toString());
        Long boardId = boardService.findByHostId(memberDto.getMemberId());
        log.info("boardId = {}",boardId);
        return guestJpaRepository.findAllByBoardId(boardId);
    }

    public boolean findByMemberId(Long boardId, String memberId){
        List<Guest> guests = guestJpaRepository.findAllByBoardId(boardId);
        return guests.stream().anyMatch(g -> g.getGuestId().equals(memberId));
    }
}
