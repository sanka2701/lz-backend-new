package sk.liptovzije.application.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import sk.liptovzije.application.file.File;
import sk.liptovzije.application.post.Post;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "article")
public class Article extends Post {

    private Article() {
        super();
    }

    public static class Builder {
        private Long id;
        private Long ownerId;
        private String title;
        private File thumbnail;
        private String content;

        public Builder(long ownerId, String title, String content) {
            this.ownerId = ownerId;
            this.title = title;
            this.content = content;
        }

        public Builder thumbnail(File thumbnail){
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Article build() {
            Article article = new Article();

            article.setId(id);
            article.setOwnerId(ownerId);
            article.setTitle(title);
//            article.setThumbnail(thumbnail);
            article.setContent(content);

            return article;
        }
    }
}
