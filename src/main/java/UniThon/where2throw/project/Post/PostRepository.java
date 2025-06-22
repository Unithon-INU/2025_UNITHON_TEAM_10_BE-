package UniThon.where2throw.project.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    Page<PostEntity> findByCategory(String category, Pageable pageable);

    @Query("""
      SELECT p 
      FROM PostEntity p
      JOIN p.author a
      WHERE p.category = :category
        AND (
          LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%'))
          OR LOWER(p.content) LIKE LOWER(CONCAT('%', :kw, '%'))
          OR LOWER(a.username) LIKE LOWER(CONCAT('%', :kw, '%'))
        )
    """)
    Page<PostEntity> searchByKeyword(
            @Param("category") String category,
            @Param("kw") String keyword,
            Pageable pageable
    );
}