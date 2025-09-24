package br.com.fiap.smartlist.list;

import br.com.fiap.smartlist.item.ProductItem;
import br.com.fiap.smartlist.user.User;
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

    private String status;

    private BigDecimal total;

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
