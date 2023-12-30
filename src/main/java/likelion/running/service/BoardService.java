package likelion.running.service;

import likelion.running.domain.board.Board;
import likelion.running.domain.board.BoardJpaRepository;
import likelion.running.domain.guest.Guest;
import likelion.running.domain.guest.GuestJpaRepository;
import likelion.running.web.dto.BoolRespose;
import likelion.running.web.dto.boardDto.BoardForm;
import likelion.running.web.dto.boardDto.ComingDto;
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

    public Optional<Board> openRunning(BoardForm boardForm) {
        Board board = boardJpaRepository.save(Board.builder()
                .hostId(boardForm.getHostId())
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .totalMember(boardForm.getTotalMember())
                .repeat_(boardForm.getRepeat())
                .flag(boardForm.getFlag())
                .runTime(boardForm.getRunTime())
                .walkTime(boardForm.getWalkTime())
                .playTime(boardForm.getPlayTime())
                .status(boardForm.getStatus())
                .normalTime(boardForm.getTime())
                .guests(boardForm.getGuest())
                .build());
        log.info("board 생성 {}",board.getTitle());
        return Optional.of(board);
    }

    public Optional<Board> findByBoardId(Long boardId) {
        return boardJpaRepository.findBoardById(boardId);
    }

    public Long findByHostId(String hostId) {
        Optional<Board> board = boardJpaRepository.findBoardByHostId(hostId);
        board.ifPresent(value -> log.info(String.valueOf(value.getId())));

        return board.map(Board::getId).orElse(null);
    }

    public List<Board> findAllBoardByTime(LocalDate date) {
        return boardJpaRepository.findAllByNormalTime(date);
    }

    public List<ComingDto> findAllBoardByMemberId(String memberId) {

        List<Guest> guests = guestJpaRepository.findByGuestId(memberId);
        List<Board> boards = boardJpaRepository.findAllByHostIdAndNormalTimeIsAfter(memberId, LocalDate.now());
        List<ComingDto> list = new ArrayList<>();
        log.info("isEmpty? "+String.valueOf(boards.isEmpty()));

        for (Board board : boards) {
            log.info(String.valueOf(board.getId()));
            LocalDate time = board.getNormalTime();
            if(time!=null){
                list.add(ComingDto.builder()
                        .title(board.getTitle())
                        .flag(board.getFlag())
                        .time(board.getNormalTime())
                        .build());
            }
        }
        log.info("Guests is Empty? "+String.valueOf(guests.isEmpty()));
        for (Guest guest : guests) {
            Optional<Board> board = boardJpaRepository.findBoardById(guest.getBoard().getId());
            log.info(guest.getGuestId());
            board.ifPresent(value-> {
                if(value.getNormalTime()!=null && value.getNormalTime().isAfter(LocalDate.now()))
                    list.add(ComingDto.builder()
                            .title(value.getTitle())
                            .flag(value.getFlag())
                            .time(value.getNormalTime())
                            .build());
            });
        }

        Comparator<ComingDto> comparator = Comparator.comparing(ComingDto::getTime, Comparator.nullsLast(Comparator.naturalOrder()));
        list.sort(comparator);

        return list;
    }

    public String removeBoard(Long boardId) {
        boardJpaRepository.deleteById(boardId);
        return "ok";
    }

    @Transactional
    public BoolRespose editBoard(Long boardId, EditBoardDto editBoardDto) {
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
                .playTime(editBoardDto.getPlayTime())
                .status(editBoardDto.getStatus())
                .guests(editBoardDto.getGuests())
                .build();
        board.edit(editNew);
        log.info("board title {}", board.getTitle());
        log.info("board content {}", board.getContent());
        return BoolRespose.builder()
                .ok(true)
                .build();
    }

    public void increaseMember(GuestDto guestDto) {
        Optional<Board> board = boardJpaRepository.findBoardById(guestDto.getBoardId());
        board.ifPresent(Board::increase);
        board.ifPresent(boardJpaRepository::save);
    }
}
