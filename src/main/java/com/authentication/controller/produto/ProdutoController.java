package com.authentication.controller.produto;

import com.authentication.controller.produto.request.ProdutoRequest;
import com.authentication.controller.produto.response.ProdutoResponse;
import com.authentication.domain.dto.ProdutoDTO;
import com.authentication.domain.mapper.ProdutoMapper;
import com.authentication.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("produto")
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoMapper mapper;



    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@RequestBody ProdutoRequest request){
        ProdutoDTO produtoDTO = service.criarProduto(mapper.requestToDto(request));
        ProdutoResponse response = mapper.dtoToResponse(produtoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarProduto(@PathVariable Long id){
        ProdutoDTO produtoDTO = service.buscarProduto(id);
        ProdutoResponse response = mapper.dtoToResponse(produtoDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
