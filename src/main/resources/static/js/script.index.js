
// Очищает форму при добавлении нового
function clearForm(textLabelForm=''){
    const form = document.getElementById('edit_modal_form');
     form.querySelectorAll('input:not([type="checkbox"])').forEach(input => input.value = '');
     form.querySelectorAll('select').forEach(select => select.selectedIndex= 0);

     //form.querySelectorAll('.form-check-input').forEach(check=> {
     //   check.value = false
     //});
      // ПРАВИЛЬНАЯ очистка чекбоксов
     form.querySelectorAll('.form-check-input').forEach(check => {
         check.checked = false; // Снимаем галочку
         // Значение (value) менять НЕЛЬЗЯ, там лежит ID бокса!
     });

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

    //const idClient  = button.getAttribute('data-id-client');
    const rawData  = button.getAttribute('data-id');
    let ids;

    try {
        // Пробуем распарсить как JSON (например, "[1,2,3]")
        ids = JSON.parse(rawData);
        // Если распарсилось число, а не массив, оборачиваем его
        if (!Array.isArray(ids)) {
            ids = [ids];
        }
    } catch (e) {
        // Если это не JSON (простая строка/число), создаем массив из 1 элемента
        // Используем Number(), чтобы в массиве были числа, а не строки
        ids = rawData ? [Number(rawData)] : [];
    }

    // Создаем временный элемент для загрузки нового содержимого
    //fetch(`/box/fragments/account_edit_modal?clientId=${idClient}&boxIds=${ids.join(',')}`)
    fetch(`/box/fragments/account_edit_modal?boxIds=${ids.join(',')}`)
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
    const sumInput = document.getElementById('data-sum-amount'); // Поле суммы из инпута
    const checkedBoxes = document.querySelectorAll('.box-checkbox:checked');

    if(isCountElem!=null){
        isCountElem.textContent = checkedBoxes.length;
        //const checkedBoxes = document.querySelectorAll('.box-checkbox:checked');
        //document.getElementById('selectedCount').textContent = checkedBoxes.length;
    }

    // Обновляем сумму
        if (sumInput != null) {
            let totalSum = 0;
            checkedBoxes.forEach(cb => {
                // Читаем data-price, который мы добавили в HTML
                const price = parseFloat(cb.getAttribute('data-price')) || 0;
                totalSum += price;
            });
            sumInput.value = totalSum.toFixed(2);
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
        resultsContainer.style.display = 'none'; // Скрываем контейнер
        noResults.style.display = 'none';
        return;
    }

    fetch(`/client/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(clients => {
            resultsContainer.innerHTML = '';

            if (clients.length === 0) {
                noResults.style.display = 'block';
                resultsContainer.style.display = 'none'; // Скрываем, если пусто
                return;
            }

            noResults.style.display = 'none';
            resultsContainer.style.display = 'block'; // Показываем список результатов

            clients.forEach(client => {
                // Создаем элемент списка
                const formattedDate = client.birthDate
                    ? new Date(client.birthDate).toLocaleDateString('ru-RU')
                    : 'дата не указана';
                const listItem = document.createElement('a');
                listItem.className = 'list-group-item list-group-item-action';
                listItem.href = '#';
                listItem.innerHTML = `
                        <div class="d-flex w-200 justify-content-between">
                        <h6 class="mb-1">${client.lastName} ${client.firstName} ${client.patronymic} ${formattedDate}</h6>
                    </div>

                `;

                listItem.onclick = function(e) {
                    e.preventDefault();

                    // 1. Обновляем инфо в форме (ваш метод)
                    updateClientInfo(client.id);

                    // 2. ЗАПИСЫВАЕМ ID В СКРЫТОЕ ПОЛЕ (чтобы форма отправила его на сервер)
                    // у вашего <input type="hidden"> id именно 'selected-client-id'
                    const hiddenInput = document.getElementById('data-client-id');
                    if (hiddenInput) {
                        hiddenInput.value = client.id;
                    }

                    // 2. Подставляем имя в инпут поиска для наглядности
                    const input = document.getElementById('clientSearchInput');
                    input.value = `${client.lastName} ${client.firstName} ${client.patronymic} ${formattedDate}`;
                    //input.readOnly = true;
                    input.classList.add('bg-light');

                    // 3. Скрываем список результатов
                    resultsContainer.style.display = 'none';
                };

                resultsContainer.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error('Ошибка при поиске:', error);
            noResults.innerHTML = '<p class="text-danger small">Ошибка поиска</p>';
            noResults.style.display = 'block';
        });
}

// Чтобы список скрывался при клике мимо него
document.addEventListener('click', function(e) {
    const container = document.getElementById('searchResults');
    const input = document.getElementById('clientSearchInput');
    if (container && !container.contains(e.target) && e.target !== input) {
        container.style.display = 'none';
    }
});

function searchClients_2() {
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

// Информация ниже окна поиска
function clearClientInfo() {
    updateClientInfo(-1)
}

// Информация в строке поиска
function clearClientSearch() {
    updateClientInfo(-1)
    const input = document.getElementById('clientSearchInput');
    input.value = ``;
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

function removeBoxFromRent(button) {

    const boxId = button.getAttribute('data-id');
    if (!confirm('Удалить этот бокс из аренды?')) return;

    fetch(`/box/rent-remove/${boxId}`, {
            method: 'POST'
        })
        .then(response => {
            if (!response.ok) throw new Error('Ошибка при удалении');
            return response.text();
        })
        .then(html => {
            // Заменяем содержимое контейнера новым HTML от сервера
            // Сервер сам пришлет обновленный список, сумму и счетчик
            document.getElementById('rented-boxes-container').innerHTML = html;
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Не удалось удалить бокс');
        });
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