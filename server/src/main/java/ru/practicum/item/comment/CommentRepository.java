package ru.practicum.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllCommentByItem(Item item);
}
