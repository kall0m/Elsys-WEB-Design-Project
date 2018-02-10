package healthblog.services;

import healthblog.models.Article;
import healthblog.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleServiceStubImpl implements ArticleService {
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceStubImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public boolean articleExists(Integer id) {
        return articleRepository.exists(id);
    }

    public Article findArticle(Integer id) {
        return articleRepository.findOne(id);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public void saveArticle(Article article) {
        articleRepository.saveAndFlush(article);
    }
}
