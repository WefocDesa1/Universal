package com.sync.dto.magiis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Wrapper de respuesta paginada de Magiis.
 *
 * Endpoints de viajes (recientes / histórico): datos en "page.content"
 * Endpoints de cuentas corrientes: datos en root "content"
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagiisPagedResponse<T> {

    @JsonProperty("page")
    private PageWrapper<T> page;

    @JsonProperty("content")
    private List<T> rootContent;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("totalPages")
    private Integer rootTotalPages;

    @JsonProperty("currentPage")
    private Integer rootCurrentPage;

    public List<T> getContent() {
        if (page != null) return page.getContent();
        return rootContent;
    }

    public int getTotalPages() {
        if (page != null && page.getTotalPages() != null) return page.getTotalPages();
        return rootTotalPages != null ? rootTotalPages : 1;
    }

    public int getCurrentPage() {
        if (page != null && page.getCurrentPage() != null) return page.getCurrentPage();
        return rootCurrentPage != null ? rootCurrentPage : 1;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageWrapper<T> {
        @JsonProperty("content")
        private List<T> content;

        @JsonProperty("currentPage")
        private Integer currentPage;

        @JsonProperty("totalPages")
        private Integer totalPages;
    }
}
