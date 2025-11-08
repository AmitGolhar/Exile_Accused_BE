package com.exileaccused.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exileaccused.entity.ExileData;
import com.exileaccused.entity.ExileRecord;
import com.exileaccused.repository.ExileDataRepository;
import com.exileaccused.repository.ExileRecordRepository;
import com.exileaccused.service.ExcelService;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = { "*", "http://exiled.pnl4u.in" })
public class ExcelController {

	@Autowired
	private ExcelService excelService;
	@Autowired
	private ExileDataRepository exileRepo;
	@Autowired
	private ExileRecordRepository recordRepo;

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file,
			@RequestParam(defaultValue = "admin") String uploadedBy) {

		try {
			excelService.processAndSaveExcel(file, uploadedBy);
			return ResponseEntity.ok("Excel uploaded and processed successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	/**
	 * ✅ Get All Uploaded Excel Data with Records
	 */
	@GetMapping("/all")
	public ResponseEntity<List<ExileData>> getAllUploads() {
		List<ExileData> allUploads = exileRepo.findAll();

		if (allUploads.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(allUploads);
	}

	/**
	 * ✅ Get Single Upload by ID
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ExileData> getUploadById(@PathVariable Long id) {
		return exileRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * ✅ Upload photo for specific record
	 */
	@PostMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

		try {
			ExileRecord record = recordRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Record not found: " + id));

			record.setPhoto(file.getBytes()); // if storing as BLOB
			recordRepo.save(record);

			return ResponseEntity.ok("Photo uploaded successfully for record ID " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading photo: " + e.getMessage());
		}
	}

	/**
	 * ✅ Retrieve photo (for display)
	 */
	@GetMapping("/{id}/photo")
	public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
		return recordRepo.findById(id).filter(r -> r.getPhoto() != null)
				.map(r -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(r.getPhoto()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<ExileRecord> updateRecord(@PathVariable Long id, @RequestBody ExileRecord updatedRecord) {

		Optional<ExileRecord> optionalRecord = recordRepo.findById(id);
		if (!optionalRecord.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		ExileRecord existing = optionalRecord.get();
		 existing.setPoliceStation(updatedRecord.getPoliceStation());
		existing.setNameOfAccused(updatedRecord.getNameOfAccused());
		existing.setAddress(updatedRecord.getAddress());
		existing.setMcoaSection(updatedRecord.getMcoaSection());
		existing.setPeriodOfExile(updatedRecord.getPeriodOfExile());
		  existing.setDateOfExileOrder(updatedRecord.getDateOfExileOrder());
		  existing.setDateOfExecutionOfExileOrder(updatedRecord.getDateOfExecutionOfExileOrder());
		  existing.setEndDateOfExilePeriod(updatedRecord.getEndDateOfExilePeriod());
		existing.setMobileNumber(updatedRecord.getMobileNumber());

		recordRepo.save(existing);
		return ResponseEntity.ok(existing);
	}
	
	@PostMapping("/manual-entry")
	public ResponseEntity<?> addManualRecord(@RequestBody ExileRecord record) {

	    ExileData defaultData = exileRepo.findById(1L)
	            .orElseGet(() -> {
	                ExileData data = new ExileData();
	                data.setId(1L);
	                data.setUploadedBy("Manual Entry Default");
	                return exileRepo.save(data);
	            });

	    Long latestSrNo = recordRepo.findMaxSrNo();
	    long nextSrNo = (latestSrNo != null ? latestSrNo + 1 : 1);

	    record.setSrNo(String.valueOf(nextSrNo));
	    record.setExileData(defaultData);

	    ExileRecord savedRecord = recordRepo.save(record);
	    return ResponseEntity.ok(savedRecord);
	}


}
