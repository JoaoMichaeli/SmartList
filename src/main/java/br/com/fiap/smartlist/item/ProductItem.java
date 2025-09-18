package br.com.fiap.smartlist.item;

import br.com.fiap.smartlist.list.ShoppingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{list.name.notblank}")
    private String name;

    private Long quantity;

    private BigDecimal price;

    private Boolean checked = false;

    @ManyToOne
    private ShoppingList shoppingList;

}

