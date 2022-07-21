package com.example.smartmyhome.controller;

import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.TodoDTO;
import com.example.smartmyhome.model.todo.TodoEntity;
import com.example.smartmyhome.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = service.testService(); // 테스트 서비스 이용
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    /*ResponseEntity는 'data, header(생략가능), 상태코드' 를 리턴해주기 위해서
    사용하는 것이고, header 정보는 생략가능합니다.*/
    // TODO: 2022-05-15 createcontroller
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){ // json형태 dto 가져옴
        try{
            /*String temporaryUserId = "temporary-user"; // temporary user id.*/
            // 1. TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);
            // 2. id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문이다.
            /*해당 객체의 아이디를 null 값으로 초기화*/
            entity.setId(null);
            /*3. 임시 사용자 아이디로 설정해 준다.
             * 지금은 인증과 인가기능이 없으므로 한 사용자만 로그인 없이 사용할 수 있다.
             * */
            entity.setUserId(userId);
            // 4. 서비스를 이용해 Todo엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            // 5. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 6. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 7. ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            // 8. 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴한다.(try catch에 잡힌 에러를 body로 전송)
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    /*상단의 createTodo를 먼저 진행하고 값을 다시 가져오는 방식 사용해야됨 */
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){ // 되찾아오다 -> 테스트
        /*String temporaryUserId = "temporary-user";*/
        // TODO: 2022-05-15 Entity값을 temporaryUserId에 맞는 값을 찾아 가져오기 즉 레코드를 가져오기
        // (1) 서비스 메서드의 retrieve() 메서드를 사용해 Todo리스트를 가져온다(Repository의 findByUserID)
        // TodoEntity형식의 리스트에 entities를 담는다 애초에 Repositiry에 리스트로 약속함
        // 따라서 가져올때 리스트로 가져와서 다시 리스트를 entities객체로 선언해서 가져옴
        List<TodoEntity> entities = service.retrieve(userId);

        // TODO: 2022-05-15 entity값을 dto로 변환하기
        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 반환한다.
        // map(Function < T, R > mapper) : 데이터를 특정 데이터로 변환
        // ex) https://sabarada.tistory.com/40?category=815130 steam api 사용방법
        // steam으로 변환후 -> TodoDTO 객체 형식으로 생성-> collect함수를 통해 list로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // TODO: 2022-05-15 변환된 dto값을 ResponseDTO의 data에 전달해서 body애 반환할수 있도록 한다.
        // (3) 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다. ResponseDTO자체는 사용자에게 반응값을 주기위함
        // ResponseDTO자체는 제네릭을 어떤 객체로 사용해도 상관 없도록 만듬 -> 반응-> 성공및 실패를 알기 위해 한번 거침
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (4) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    // TODO: 2022-05-15 updatecontroller
    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId){
        /*String temporaryUserId = "temporary-user"; // temporary user id.*/

        // 1. dto를 entity로변환
        TodoEntity entity = TodoDTO.toEntity(dto);

        // 2. id를 temporaryUserId로 초기화 한다.
        entity.setUserId(userId);

        // 3. 서비스를 이용해 entity를 업데이트
        List<TodoEntity> entities = service.update(entity);

        // 4. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        /*리스트로 바꾸려면 -> entity->stream->TodoDTO의 형태의 메모리 생성 매핑-> TodoDTO 리스트 */
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 5. 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId){
        try{
            /*String temporaryUserId = "temporary-user"; //temporary user id.*/

            // 1. TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2. 임시 사용자 아이디를 설정해 준다.
            entity.setUserId(userId);

            // 3. 서비스를 이용해 entity를 삭제한다.
            List<TodoEntity> entities = service.delete(entity);

            // 4. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 5. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // 6. ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            // 7. 혹시 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            // 반환할때는 ResponseEntity를 활용해서 상태코드,header,data를 나타내기 위해 사용한다.
            return ResponseEntity.badRequest().body(response);
        }
    }

}
