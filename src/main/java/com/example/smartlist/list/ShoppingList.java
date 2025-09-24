package com.example.smartlist.list;

import com.example.smartlist.item.ProductItem;
import com.example.smartlist.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{item.title.notblank}")
    private String title;

    @NotBlank(message = "{item.status.notblank}")
    private String status;

    @Transient
    private BigDecimal total;

    @Transient
    private long checkedCount;

    @Transient
    public long getCheckedCount() {
        if (items == null) return 0;
        return items.stream().filter(ProductItem::isChecked).count();
    }

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItem> items;
}
