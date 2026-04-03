
// Очищает форму при добавлении нового
function clearForm(textLabelForm=''){
    const form = document.getElementById('edit_modal_form');
     form.querySelectorAll('input').forEach(input => input.value = '');
     form.querySelectorAll('select').forEach(select => select.selectedIndex= 0);
     form.querySelectorAll('.form-check-input').forEach(check=> check.value = false);
     updateSelectedCount_account();
      if(textLabelForm!=''){
         document.getElementById('objectModalLabel').textContent = textLabelForm;
      }
}

// Устанавливает ID для удаления
function setDeleteId(button) {
    const id = button.getAttribute('data-id'); // id объекта для удаления
    const object_from_url_delete = button.getAttribute('data-object-from-url-delete'); // имя объекта для удаления
    const data_form_question_object = button.getAttribute('data-form-question-object'); // имя объекта, например БОКС, СЧЕТ, Тариф
    const data_form_question_name = button.getAttribute('data-form-question-name'); // имя из удаляемой записи

    document.getElementById('textFormQuestion').textContent = 'Вы действительно хотите удалить '+data_form_question_object;
    document.getElementById('objectNameFormQuestion').textContent = data_form_question_name;
    document.getElementById('confirmDeleteBtn').href = '/'+object_from_url_delete+'/delete?id=' + id;
}

// Заполнение формы по атрибутам
function getArrayAtributesFromButton(button, prefix='data-', strReplace='',toCamelCase=false){

    const dataAttributes = {};

    // Перебираем все атрибуты элемента
    Array.from(button.attributes).forEach(attr => {
        // Фильтруем только data-атрибуты (начинающиеся с "data-")
        if (attr.name.startsWith(prefix)) {
            // Преобразуем имя атрибута из kebab-case в camelCase для удобства
            const key = attr.name.replace(strReplace, '');  // Убираем префикс "data-"
            // если нужно сделать забором, то удалим тире и пробелы и сделаем первую букву заглавной
            if(toCamelCase){
                key.replace(/-([a-z])/g, (match, letter) => letter.toUpperCase()); // kebab-case → camelCase
            }

            dataAttributes[key] = attr.value;
        }
    });

    // Выводим объект с данными в консоль для отладки
    //console.log('Data attributes:', dataAttributes);
    return dataAttributes;
}
function setAttribytesFromArray(dataAttributes){

    // Автоматически заполняем поля формы по соответствию ключей
    Object.keys(dataAttributes).forEach(key => {
        const element = document.getElementById(key);
        if (element) {
            element.value = dataAttributes[key];
        }
    });
}



// Устанавливает ID для аренды
function rentBoxId(button) {

    const id = button.getAttribute('data-id');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/box/fragments/account_edit_modal?id='+id)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;

            // Установка выбранных боксов из boxes-edit
            setCheckedBoxes_account();
            // Обновляем счётчик
            updateSelectedCount_account();
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}
// Заполняет форму редактирования
function editModalForm(button, fragmentPath, textLabelForm='', isAccountEdit=false, prefix='data-',strReplace='' ,toCamelCase=false){
    const id = button.getAttribute('data-id');

    // Создаем временный элемент для загрузки нового содержимого
    fetch(fragmentPath+'?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            // Получаем все data-атрибуты кнопки и заполняим ими форму
            const dataAttributes = getArrayAtributesFromButton(button, prefix, strReplace, toCamelCase);
            setAttribytesFromArray(dataAttributes);
            if(textLabelForm!=''){
                document.getElementById('objectModalLabel').textContent = textLabelForm;
            }
            if(isAccountEdit){
                // Установка выбранных боксов из boxes-edit
                setCheckedBoxes_account();
                // Обновляем счётчик
                updateSelectedCount_account();
            }
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}


// Обновление счётчика выбранных боксов
function updateSelectedCount_account() {
    const isCountElem = document.getElementById('selectedCount');
    if(isCountElem!=null){
        const checkedBoxes = document.querySelectorAll('.box-checkbox:checked');
        document.getElementById('selectedCount').textContent = checkedBoxes.length;
    }
}
// Выделение всех/снятие всех чекбоксов
function toggleAllBoxes_account(source) {
    setCheckedBoxesChecked_account(source.checked)
}
function setCheckedBoxesChecked_account(checked){
   const checkboxes = document.querySelectorAll('.box-checkbox');
       checkboxes.forEach(checkbox => {
           checkbox.checked = checked;
       });
       updateSelectedCount_account();
}
// Установка выбранных боксов из boxes-edit
function setCheckedBoxes_account() {
    const boxesEditInput = document.getElementById('boxes-form-edit');

    if (!boxesEditInput || !boxesEditInput.value) return;

    try {
        // Парсим значение из скрытого поля (предполагаем, что это JSON массив ID)
        const selectedBoxIds = JSON.parse(boxesEditInput.value);
        if (!Array.isArray(selectedBoxIds)) return;

        // Находим все чекбоксы и устанавливаем состояние
        const checkboxes = document.querySelectorAll('.box-checkbox');
        checkboxes.forEach(checkbox => {
            if (selectedBoxIds.includes(parseInt(checkbox.value))) {
                checkbox.checked = true;
                console.log('Установлен checkbox:', parseInt(checkbox.value));
            }
        });

        // Обновляем счётчик
        //updateSelectedCount_account();
    } catch (e) {
        console.error('Error parsing boxes-edit value:', e);
    }
}

async function saveRandomClient() {

    try {
        const response = await fetch('/client/random', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        });

        if (response.ok) {
            // Перезагружаем страницу после успешного создания
            window.location.href = '/client';
        } else {
            alert('Ошибка при создании клиента');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Произошла ошибка при создании клиента');
    }

}

//// Добавляем кнопку поиска в хедер
//document.querySelector('.col-md-3.text-end').insertAdjacentHTML('beforebegin', `
//    <div class="col-md-3 text-end">
//        <button class="btn btn-outline-secondary me-2"
//                type="button"
//                data-bs-toggle="modal"
//                data-bs-target="#searchClientModal">
//            <i class="bi bi-search"></i> Поиск клиента
//        </button>
//    </div>
//`);

function searchClients() {
    const query = document.getElementById('clientSearchInput').value.toLowerCase();
    const resultsContainer = document.getElementById('searchResults');
    const noResults = document.getElementById('noResults');

    if (query.length < 2) {
        resultsContainer.innerHTML = '';
        noResults.style.display = 'none';
        return;
    }

    // Очистка предыдущих результатов
    resultsContainer.innerHTML = '';

    // Выполнение поиска через AJAX
    fetch(`/client/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(clients => {
            if (clients.length === 0) {
                noResults.style.display = 'block';
                return;
            }

            noResults.style.display = 'none';

            clients.forEach(client => {
                const listItem = document.createElement('a');
                listItem.className = 'list-group-item list-group-item-action';
                listItem.href = '#';
                // Вывод представления о клинетах
                listItem.innerHTML = `
                    <div class="d-flex w-100 justify-content-between">
                        <h6 class="mb-1">${client.lastName} ${client.firstName}</h6>
                        <small>${client.phoneNumber}</small>
                    </div>
                    <p class="mb-1">${client.emailAddress}</p>
                    <small>${client.address}</small>
                `;

                // добавление клиента в боковую панель
                listItem.onclick = function(e) {
                    e.preventDefault();
                    //selectClient(client.id);
                    updateClientInfo(client.id);
                    bootstrap.Modal.getInstance(document.getElementById('searchClientModal')).hide();
                };

                resultsContainer.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error('Ошибка при поиске:', error);
            noResults.innerHTML = '<p class="text-danger">Ошибка при выполнении поиска</p>';
            noResults.style.display = 'block';
        });
}

function updateClientInfo(clientId) {
    const container = document.getElementById('client-info-container');

    fetch(`/client/client-details/${clientId}`)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text();
        })
        .then(html => {
            // Заменяем содержимое контейнера новым HTML от сервера
            container.innerHTML = html;
        })
        .catch(error => console.error('Ошибка:', error));
}

function clearClientInfo() {
    updateClientInfo(-1)
}

function updateRentedBoxes(button) {

    const boxId = button.getAttribute('data-id');
    fetch(`/box/rent/${boxId}`, {
            method: 'POST'
        })
        .then(response => {
            if (!response.ok) throw new Error('Ошибка добавления');
            return response.text();
        })
        .then(html => {
            // Обновляем содержимое контейнера в правой колонке
            document.getElementById('rented-boxes-container').innerHTML = html;
        })
        .catch(error => console.error('Ошибка:', error));
}


function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU');
}

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");
}


function showClientAccounts() {
    // Логика отображения счетов клиента
    alert('Функция просмотра счетов клиента');
}

function loadPage(event, page) {
    event.preventDefault();

    const search = document.querySelector('input[name="search"]')?.value || '';
    const size = 6;

    fetch(`/box/fragments/box-grid?page=${page}&size=${size}&search=${encodeURIComponent(search)}`)
        .then(response => response.text())
        .then(html => {

            document.getElementById('box-grid-container').outerHTML = html;

            updatePagination(page);
        })
        .catch(error => {
            console.error('Ошибка загрузки страницы:', error);
        });
}

function updatePagination(currentPage) {
    const paginationItems = document.querySelectorAll('.pagination .page-item');
    paginationItems.forEach(item => {
        const link = item.querySelector('.page-link');
        if (link && link.textContent !== 'Назад' && link.textContent !== 'Вперёд') {
            const pageNum = parseInt(link.textContent) - 1;
            if (pageNum === currentPage) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        }
    });
}