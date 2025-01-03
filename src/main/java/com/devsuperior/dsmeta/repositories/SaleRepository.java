package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projections.ReportMinProjection;
import com.devsuperior.dsmeta.projections.SummaryMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    // salesSummary = Retorna o nome do vendedor e a quantidade vendida para a respectiva data.
    //SELECT SUM (TB_SALES.AMOUNT) AS TOTAL, TB_SELLER.NAME AS SELLERNAME FROM TB_SALES JOIN TB_SELLER ON TB_SALES.SELLER_ID = TB_SELLER.ID WHERE TB_SALES.DATE BETWEEN '2022-01-01' AND '2022-06-30' GROUP BY TB_SELLER.ID ORDER BY TB_SELLER.NAME
    @Query(nativeQuery = true, value =
            "SELECT SUM (TB_SALES.AMOUNT) AS TOTAL, " +
            "TB_SELLER.NAME AS SELLERNAME " +
            "FROM TB_SALES " +
            "JOIN TB_SELLER ON TB_SALES.SELLER_ID = TB_SELLER.ID " +
            "WHERE TB_SALES.DATE BETWEEN :minDate AND :maxDate " +
            "GROUP BY TB_SELLER.ID " +
            "ORDER BY TB_SELLER.NAME"
    )
    List<SummaryMinProjection> salesSummary(String minDate, String maxDate);

    //salesReport = Retorna vendedores e suas vendas para o nome 'Odinson'.
    //SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name as sellerName
    //FROM tb_sales JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
    //WHERE tb_sales.date BETWEEN '2022-05-01' AND '2022-05-31' AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%', 'odinson', '%'))
    @Query(nativeQuery = true, value =
            "SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name as sellerName " +
            "FROM tb_sales " +
            "JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
            "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:sellerName, '%'))",
            countQuery = "SELECT COUNT(tb_sales.id) " +
                    "FROM tb_sales " +
                    "JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
                    "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                    "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:sellerName, '%'))"
    )
    Page<ReportMinProjection> salesReport(String minDate, String maxDate, String sellerName, Pageable pageable);
}
