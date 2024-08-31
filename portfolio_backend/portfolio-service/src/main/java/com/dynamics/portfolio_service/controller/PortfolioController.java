package com.dynamics.portfolio_service.controller;



import com.dynamics.portfolio_service.dto.PortfolioDetailsDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import com.dynamics.portfolio_service.dto.response.*;
import com.dynamics.portfolio_service.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/portfolio", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/")
    public ResponseEntity<MultiplePortfolioResponseDto> getAllPortfolios(){
        List<PortfolioDto> portfolios = portfolioService.getAllPortfolios();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MultiplePortfolioResponseDto(
                        "200",
                        "success",
                        portfolios
                ));
    }

    /*
    @GetMapping("/{portfolioId}")
    public ResponseEntity<SinglePortfolioResponseDto> getPortfolioById(@PathVariable Long portfolioId){
        PortfolioDto portfolioDto = portfolioService.getByPortfolioId(portfolioId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SinglePortfolioResponseDto(
                        "200",
                        "successfull",
                        portfolioDto
                ));
    }

     */

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createPortfolio(@Valid @RequestBody PortfolioDto portfolioDto,
                                                       @RequestHeader("X-User-ID") UUID userId){
        portfolioService.createPortfolio(portfolioDto, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(
                        "201",
                        "successfully created"
                ));
    }

    /*
    @GetMapping("/details")
    public ResponseEntity<MultiplePortfolioDetailsResponseDto> getAllPortfoliosDetails(){
        List<PortfolioDetailsDto> portfolios = portfolioService.getAllPortfoliosDetails();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MultiplePortfolioDetailsResponseDto(
                        "200",
                        "success",
                        portfolios
                ));
    }

     */



    @GetMapping("/owner")
    public ResponseEntity<SinglePortfolioDetailsResponseDto> getPortfolioByUserId(@RequestHeader("X-User-ID") UUID userId) {

        PortfolioDetailsDto portfolioDetailsDto = portfolioService.getPortfolioByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SinglePortfolioDetailsResponseDto(
                        "200",
                        "successfull",
                        portfolioDetailsDto
                ));

    }


    @PostMapping("/{portfolioId}/set-access-code")
    public ResponseEntity<ResponseDto> setAccessCode(@PathVariable Long portfolioId,
                                                     @RequestParam("accessCode") String accessCode,
                                                     @RequestHeader("X-User-ID") UUID userId) {
        portfolioService.updateAccessCode(portfolioId, userId, accessCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(
                        "200",
                        "successfully updated"
                ));
    }

    @GetMapping("/{portfolioId}/verify-access-code")
    public ResponseEntity<SinglePortfolioDetailsResponseDto> verifyAccessCodeAndReturnPortfolioIfAccessIsGranted(@PathVariable Long portfolioId,
                                                        @RequestParam("accessCode") String accessCode) {

        PortfolioDetailsDto portfolioDetailsDto = portfolioService.verifyAccessCodeAndReturnPortfolioIfAccessIsGranted(portfolioId, accessCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SinglePortfolioDetailsResponseDto(
                        "200",
                        "successfull",
                        portfolioDetailsDto
                ));

    }

    @GetMapping("/access-code")
    public ResponseEntity<String> getAccessCodeByUserId(@RequestHeader("X-User-ID") UUID userId) {
        String accessCode = portfolioService.getAccessCodeByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accessCode);
    }



//tbd überlegen was brauche ich noch alles im frontend sodass ich es so machen kann das jeder ein portfolio sehen kann wenn er den coede hat und vlt das man aussuchen kann zwischen den portfolios und da dann eben den code eingibt oder so. 

    /*
    POST /portfolios - Erstellen eines neuen Portfolios.
    GET /portfolios - Abrufen aller Portfolios.
    GET /portfolios/{id} - Abrufen eines spezifischen Portfolios anhand seiner ID.
    PUT /portfolios/{id} - Aktualisieren eines bestehenden Portfolios.
    DELETE /portfolios/{id} - Löschen eines Portfolios.
     */
}
