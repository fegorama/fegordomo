package com.fegorsoft.fegordomo.manager.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fegorsoft.fegordomo.manager.dto.CertsDTO;
import com.fegorsoft.fegordomo.manager.model.DeviceGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/certs")
@Tag(name = "Certs", description = "API for certificates")
public class CertsController {
    private static final Logger log = LoggerFactory.getLogger(CertsController.class);

    @Value("classpath:/certs/ca.crt")
    private Resource resourceCA;

    @Value("classpath:/certs/client.crt")
    private Resource resourceClient;

    @Value("classpath:/certs/client.key")
    private Resource resourceClientKey;
    
    @Operation(summary = "Get certs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certs generated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceGroup.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Certs not found", content = @Content) })
    @GetMapping()
    public CertsDTO download() throws IOException {
        
        log.info("Reading certificates from: {}", resourceCA.getURI());
        CertsDTO certsDTO = new CertsDTO();
        certsDTO.setCA(readCert(resourceCA));
        certsDTO.setClient_crt(readCert(resourceClient));
        certsDTO.setClient_key(readCert(resourceClientKey));

        return certsDTO;
    }

    private String readCert(Resource resource) {
        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        StringBuffer sb = null;

        try {
            file = resourceCA.getFile();
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            sb = new StringBuffer();

            String l;
            while ((l = br.readLine()) != null)
                sb.append(l);

        } catch (Exception e) {
            log.error("Error loading certificate: {}", resource.getFilename());

        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }

            } catch (Exception e2) {
                log.error("Error closing file: {}", resource.getFilename());
            }
        }

        return sb.toString();
    }
}
