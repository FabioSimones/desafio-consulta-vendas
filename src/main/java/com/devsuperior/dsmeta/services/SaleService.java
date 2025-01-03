package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.*;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<SaleReportDTO> getReport(String minDate, String maxDate, String sellerName, Pageable pageable){
		LocalDate[] dates = getDates(minDate, maxDate);

		LocalDate finalDate = dates[0];
		LocalDate startDate = dates[1];

		return repository.salesReport(
				startDate.toString(), finalDate.toString(), sellerName, pageable
		).map(x -> new SaleReportDTO(x));


	}

	@Transactional(readOnly = true)
	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate[] dates = getDates(minDate, maxDate);

		LocalDate finalDate = dates[0];
		LocalDate startDate = dates[1];

		return repository.salesSummary(startDate.toString(), finalDate.toString())
				.stream()
				.map(x -> new SaleSummaryDTO(x)).toList();
	}


	//Instanciando datas para caso não forem informadas.
	private LocalDate[] getDates(String minDate, String maxDate) {
		LocalDate finalDate;
		LocalDate startDate;

		if(maxDate.isEmpty()){
			finalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}
		else {
			finalDate = LocalDate.parse(maxDate);
		}

		if(minDate.isEmpty()){
			startDate = finalDate.minusYears(1L);
		}
		else{
			startDate = LocalDate.parse(minDate);
		}

		return new LocalDate[]{finalDate, startDate};

	}
}
