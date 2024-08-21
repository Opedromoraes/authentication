package com.authentication.domain.mapper;

import com.authentication.controller.produto.request.ProdutoRequest;
import com.authentication.controller.produto.response.ProdutoResponse;
import com.authentication.domain.dto.ProdutoDTO;
import com.authentication.domain.entity.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoDTO entityToDTO(Produto entity);

    Produto dtoToEntity(ProdutoDTO produtoDTO);

    ProdutoResponse dtoToResponse(ProdutoDTO produtoDTO);

    ProdutoDTO requestToDto(ProdutoRequest request);
}