package com.authentication.service;

import com.authentication.domain.dto.ProdutoDTO;
import com.authentication.domain.entity.Produto;
import com.authentication.domain.mapper.ProdutoMapper;
import com.authentication.exception.ResourceNotFoundException;
import com.authentication.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) {
        Produto produto = repository.save(mapper.dtoToEntity(produtoDTO));
        return mapper.entityToDTO(produto);
    }

    public ProdutoDTO buscarProduto(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));

        return mapper.entityToDTO(produto);
    }
}