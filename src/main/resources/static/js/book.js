$(function () {

    // 알라딘 Open API를 이용한 도서 검색
    var cover = $('#cover');
    var author = $('#author');
    var publisher = $('#publisher');
    $('#searchBook').select2({
        ajax: {
            type:'POST',
            url:'/book/search',
            dataType: 'json',
            delay: 250,
            contentType : 'application/json',
            data: function (params) {
                var query = {searchKeyword: params.term}; // 검색 키워드
                return JSON.stringify(query);
            },
            processResults: function (data) {
                return {
                    results: $.map(data.item, function (item) {
                        var option = item.title; // 도서명

                        // 이미지 경로가 있는 경우 이미지를 추가
                        if (item.cover) {
                            option = option + '<img src="' + item.cover + '" alt="Book Image" class="book-image"/>';
                        }

                        return {
                            text: option,
                            id: item.isbn,
                            author:item.author,
                            publisher:item.publisher,
                            cover:item.cover
                        };
                    })
                };
            },
            cache: true // 결과를 캐시하여 중복 요청을 방지
        },
        placeholder: '도서명을 검색해주세요:)',
        minimumInputLength: 1,
        escapeMarkup: function (markup) {
            return markup; // 템플릿을 HTML 이스케이프 처리하지 않도록 설정
        }
    }).on('select2:select', function (e) {
        // 선택한 옵션의 이미지를 표시할 요소에 이미지 업데이트
        var selectedOption = e.params.data;
        if (selectedOption.cover) {
            cover.attr('src', selectedOption.cover);
        } else {
            // 이미지가 없는 경우 이미지 요소 숨기기
            cover.hide();
        }

        author.text(selectedOption.author);
        publisher.text(selectedOption.publisher);
    }).on('select2:unselect', function () {
        // 옵션 선택 해제 시 이미지 요소 숨기기
        cover.hide();
    });

    // 체크박스와 텍스트 요소 선택
    $('input[type="checkbox"]').change(function () {
        var clickedIndex = $(this).parent().index(); // 부모(label) 엘리먼트의 인덱스를 가져옵니다.
        $('input[type="checkbox"]').each(function (index) {
            if (index <= clickedIndex) {
                $(this).prop('checked', true);
                $(this).siblings('span').text('❤️');
            } else {
                $(this).prop('checked', false);
                $(this).siblings('span').text('🤍');
            }
        });
    });
})