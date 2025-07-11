package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, UpdateItemDTO itemDTO) {
        // 변경 감지를 통한 데이터 변경
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(itemDTO.getName());
        findItem.setPrice(itemDTO.getPrice());
        findItem.setStockQuantity(itemDTO.getStockQuantity());

        // 실무에선
        // findItem.change(price, name, stockQuantity) 이런식으로

    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
