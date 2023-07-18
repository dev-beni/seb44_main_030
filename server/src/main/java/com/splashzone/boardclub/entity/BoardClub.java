package com.splashzone.boardclub.entity;

import com.splashzone.audit.Auditable;
import com.splashzone.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class BoardClub extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardClubId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    @Lob
    private String content;

    @Column(name = "DUE_DATE", nullable = false)
    private LocalDate dueDate; // 모집일이 지나면 자동으로 모집 종료되게 처리, 현재보다 이전일은 지정 못하게 처리

    @Column(name = "CAPACITY", nullable = false)
    private Integer capacity;

    @Column(name = "CONTACT", nullable = false, length = 100)
    private String contact;

    @Column(name = "VIEW", nullable = false, columnDefinition = "integer default 0")
    private int view;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    @Column(name = "BOARD_CLUB_STATUS", nullable = false)
    private BoardClubStatus boardClubStatus = BoardClubStatus.BOARD_CLUB_RECRUITING;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "boardClub", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<BoardClubTag> boardClubTags = new ArrayList<>();

    public enum BoardClubStatus {
        BOARD_CLUB_RECRUITING("모집 중"),
        BOARD_CLUB_COMPLETED("모집 완료"),
        BOARD_CLUB_CANCEL("모집 취소");

        @Getter
        private String status;

        BoardClubStatus(String status) {
            this.status = status;
        }
    }

    public void changeBoardClub(BoardClub boardClub, List<BoardClubTag> clubTags) {
        if (!boardClub.getTitle().isEmpty()) {
            this.title = boardClub.getTitle();
        }
        if (!boardClub.getContent().isEmpty()) {
            this.content = boardClub.getContent();
        }
        if (boardClub.getDueDate() != null) {
            this.dueDate = boardClub.getDueDate();
        }
        if (boardClub.getCapacity() != null) {
            this.capacity = boardClub.getCapacity();
        }
        if (!boardClub.getContact().isEmpty()) {
            this.contact = boardClub.getContact();
        }
        if (boardClub.getBoardClubStatus() != null) {
            this.boardClubStatus = boardClub.getBoardClubStatus();
        }

        this.boardClubTags.clear();
        this.boardClubTags.addAll(clubTags);
    }

    public void changeBoardClubStatus(BoardClubStatus boardClubStatus) {
        this.boardClubStatus = boardClubStatus;
    }
}