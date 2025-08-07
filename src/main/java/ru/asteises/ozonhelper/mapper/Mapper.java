package ru.asteises.ozonhelper.mapper;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.asteises.ozonhelper.enums.CatalogStatus;
import ru.asteises.ozonhelper.enums.UserRole;
import ru.asteises.ozonhelper.enums.UserStatus;
import ru.asteises.ozonhelper.model.RegisterUserData;
import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.model.entities.ProductQuantEntity;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class Mapper {

    public static UserEntity mapUser(User user) {
        return UserEntity.builder()
                .telegramUserId(user.getId())
                .role(UserRole.SELLER)
                .status(UserStatus.ACTIVE)
                .username(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .registeredAt(LocalDateTime.now())
                .build();
    }

    public static UserEntity mapUser(RegisterUserData registerUserData) {
        return UserEntity.builder()
                .telegramUserId(registerUserData.getTelegramUserId())
                .clientId(registerUserData.getOzonDataForm().getClientId())
                .encryptedApiKey(registerUserData.getOzonDataForm().getApiKey())
                .role(UserRole.SELLER)
                .status(UserStatus.ACTIVE)
                .username(registerUserData.getUsername())
                .firstName(registerUserData.getFirstName())
                .lastName(registerUserData.getLastName())
                .registeredAt(LocalDateTime.now())
                .build();
    }

    public static void updateUser(UserEntity userEntity, RegisterUserData registerUserData) {
        if (registerUserData.getUsername() != null) userEntity.setUsername(registerUserData.getUsername());
        if (registerUserData.getFirstName() != null) userEntity.setFirstName(registerUserData.getFirstName());
        if (registerUserData.getLastName() != null) userEntity.setLastName(registerUserData.getLastName());
    }

    /**
     * Использовать метод только для первого создания сущности!
     */
    public static UserProductCatalogEntity initMapUserProductCatalog(UserEntity userEntity) {
        return UserProductCatalogEntity.builder()
                .user(userEntity)
                .totalProducts(0)
                .status(CatalogStatus.READY)
                .build();
    }

    public static ProductEntity mapProductEntity(ProductListResponse.ProductItem item, UserProductCatalogEntity catalog) {
        ProductEntity product = ProductEntity.builder()
                .catalog(catalog)
                .productId(item.getProductId())
                .offerId(item.getOfferId())
                .hasFboStocks(item.getHasFboStocks())
                .hasFbsStocks(item.getHasFbsStocks())
                .archived(item.getArchived())
                .isDiscounted(item.getIsDiscounted())
                .lastSyncedAt(LocalDateTime.now())
                .build();

        if (item.getQuants() != null) {
            Set<ProductQuantEntity> quants = item.getQuants().stream()
                    .map(q -> ProductQuantEntity.builder()
                            .product(product)
                            .quantCode(q.getQuantCode())
                            .quantSize(q.getQuantSize())
                            .build())
                    .collect(Collectors.toSet());
            product.setQuants(quants);
        }

        return product;
    }
}
