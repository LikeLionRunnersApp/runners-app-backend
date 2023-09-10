package likelion.running.service;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.domain.guest.Guest;
import likelion.running.domain.guest.GuestJpaRepository;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.EditBoardDto;
import likelion.running.web.dto.memberDto.GuestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;
    private final GuestJpaRepository guestJpaRepository;
    @Autowired
    public BoardService(BoardJpaRepository boardJpaRepository, GuestJpaRepository guestJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
        this.guestJpaRepository = guestJpaRepository;
    }

    public Optional<Board> openRunning(BoardForm boardForm){
        Board board = boardJpaRepository.save(Board.builder()
                .hostId(boardForm.getHostId())
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .totalMember(boardForm.getTotalMember())
                .repeat(boardForm.getRepeat())
                .flag(boardForm.getFlag())
                .runTime(boardForm.getRunTime())
                .walkTime(boardForm.getWalkTime())
                .play_time(boardForm.getPlay_time())
                .status(boardForm.getStatus())
                .time(boardForm.getTime())
                .build());
        log.info("board 생성 {}",board.getTitle());
        return Optional.of(board);
    }

    public Optional<Board> findByBoardId(Long boardId){
        return boardJpaRepository.findBoardById(boardId);
    }

    public Long findByHostId(String hostId){
        Optional<Board> board = boardJpaRepository.findBoardByHostId(hostId);
        board.ifPresent(value -> log.info(String.valueOf(value.getId())));

        return board.map(Board::getId).orElse(null);
    }

    public List<Board> findAllBoardByTime(LocalDate date){
        return boardJpaRepository.findAllByTime(date);
    }

    public List<Board> findAllBoardByMemberId(String memberId){

        List<Guest> guests = guestJpaRepository.findByGuestId(memberId);

        List<Board> boards = boardJpaRepository.findAllByHostIdAndTimeIsAfter(memberId,LocalDate.now());

        List<Board> list = new ArrayList<>();
        for (Board board : boards) {
            log.info(String.valueOf(board.getId()));
            LocalDate time = board.getTime();
            if(time!=null){
                list.add(board);
            }
        }
        log.info(String.valueOf(boards.isEmpty()));

        for (Guest guest : guests) {
            Optional<Board> board = boardJpaRepository.findBoardById(guest.getBoardId());
            log.info(guest.getGuestId());
            board.ifPresent(value-> {
                if(value.getTime()!=null && value.getTime().isAfter(LocalDate.now()))
                    list.add(value);
            });
        }

        Comparator<Board> comparator = Comparator.comparing(Board::getTime,Comparator.nullsLast(Comparator.naturalOrder()));
        list.sort(comparator);

        return list;
    }

    public String removeBoard(Long boardId){
        boardJpaRepository.deleteById(boardId);
        return "ok";
    }

    @Transactional
    public boolean editBoard(Long boardId, EditBoardDto editBoardDto){
        Board board = boardJpaRepository.findBoardById(boardId).orElseThrow();
        EditBoardDto.EditBoardDtoBuilder exist = board.toEditor();
        EditBoardDto editNew = exist.title(editBoardDto.getTitle())
                .content(editBoardDto.getContent())
                .flag(editBoardDto.getFlag())
                .place(editBoardDto.getPlace())
                .runTime(editBoardDto.getRunTime())
                .walkTime(editBoardDto.getWalkTime())
                .repeat(editBoardDto.getRepeat())
                .time(editBoardDto.getTime())
                .totalMember(editBoardDto.getTotalMember())
                .play_time(editBoardDto.getPlay_time())
                .status(editBoardDto.getStatus())
                .build();
        board.edit(editNew);
        log.info("board title {}", board.getTitle());
        log.info("board content {}", board.getContent());
        return true;
    }

    public void increaseMember(GuestDto guestDto){
        Optional<Board> board = boardJpaRepository.findBoardById(guestDto.getBoardId());
        board.ifPresent(Board::increase);
        board.ifPresent(boardJpaRepository::save);
    }
}
