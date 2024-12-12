package hello.item_service.domain.web.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.item_service.domain.item.Item;
import hello.item_service.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/basic/items")
public class BasicItemController {
    private final ItemRepository itemRepository;

    @Autowired // 이렇게 쓰거나 롬복의 기능 중 하나인 @RequiredArgsConstructor 사용 - 사용하면 이거 아예 안써도 됨 하나일 경우에
    public BasicItemController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        model.addAttribute("item", itemRepository.findById(itemId));

        return "/basic/item";
    }
    
    @GetMapping("add")
    public String addForm(){
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute("item") Item item, Model model){
        itemRepository.save(item);
        model.addAttribute("item", item);
        
        return "basic/item";
    }    

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        model.addAttribute("item", itemRepository.findById(itemId));
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, @ModelAttribute("item") Item item, Model model){
        itemRepository.update(itemId, item);
        model.addAttribute("item", item); // 생략 가능. ModelAttribute가 자동으로 이 코드 만들어줌.

        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct // 초기 데이터 입력용, 생성자 주입단계에서 실행.
    public void init(){
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
