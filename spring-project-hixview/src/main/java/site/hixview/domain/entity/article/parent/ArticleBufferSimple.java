package site.hixview.domain.entity.article.parent;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class ArticleBufferSimple<E extends ArticleBufferSimple<E>> {
    protected final StringBuffer nameDatePressBuffer;
    protected final StringBuffer linkBuffer;
    @Getter protected final Integer importance;
    @Getter protected final Integer length;

    public ArticleBufferSimple(StringBuffer nameDatePressBuffer, StringBuffer linkBuffer, Integer importance) {
        this.nameDatePressBuffer = nameDatePressBuffer;
        this.linkBuffer = linkBuffer;
        this.importance = importance;
        this.length = getLinkList().size();
    }

    public String getNameDatePressString() {
        return String.valueOf(nameDatePressBuffer);
    }

    public String getLinkString() {
        return String.valueOf(linkBuffer);
    }

    public List<String> getNameDatePressList() {
        return List.of(nameDatePressBuffer.toString().split("\\R"));
    }

    public List<String> getLinkList() {
        return List.of(linkBuffer.toString().split("\\R"));
    }

    protected List<String> getParsedNameList() {
        List<String> nameList = new ArrayList<>();
        List<String> nameDatePressList = getNameDatePressList();
        for (int i = 0; i < length; i++) {
            nameList.add(nameDatePressList.get(2 * i));
        }
        return nameList;
    }

    protected List<List<String>> getParsedDatePressList() {
        List<List<String>> datePressList = new ArrayList<>();
        List<String> nameDatePressList = getNameDatePressList();
        for (int i = 0; i < length; i++) {
            datePressList.add(List.of(nameDatePressList.get(2 * i + 1)
                    .replaceAll("^\\(|\\)$", "").split(",\\s|-")));
        }
        return datePressList;
    }

    protected abstract <T extends Article<T>> List<T> getParsedArticles();

    protected abstract static class ArticleBufferSimpleBuilder {
        protected StringBuffer nameDatePressBuffer;
        protected StringBuffer linkBuffer;
        protected Integer importance;

        protected abstract ArticleBufferSimpleBuilder nameDatePressString(String nameDatePressString);

        protected abstract ArticleBufferSimpleBuilder linkString(String linkString);

        protected abstract ArticleBufferSimpleBuilder importance(Integer importance);
    }
}
