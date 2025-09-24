package com.example.smartlist.item;

import com.example.smartlist.list.ShoppingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{item.name.notblank}")
    private String name;

    @PositiveOrZero(message = "{item.price.positive}")
    private BigDecimal price;

    private boolean checked;

    @ManyToOne
    private ShoppingList shoppingList;
}
