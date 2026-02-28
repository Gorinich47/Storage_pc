
// Очищает форму при добавлении нового
function clearForm(){
    const form = document.getElementById('edit_modal_form');
     form.querySelectorAll('input').forEach(input => input.value = '');
     form.querySelectorAll('select').forEach(select => select.selectedIndex= -1);
     form.querySelectorAll('.form-check-input').forEach(check=> check.checked = false);
     updateSelectedCount_account();
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


////// box

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
            //document.querySelector('#idBox').value = button.getAttribute('data-id-box');

            // Установка выбранных боксов из boxes-edit
            setCheckedBoxes_account();
            // Обновляем счётчик
            updateSelectedCount_account();
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

// Заполняет форму редактирования бокса
function editBox(button) {

    const id = button.getAttribute('data-id');
    const idBox = button.getAttribute('data-id-box');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/box/fragments/box_edit_modal?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            document.querySelector('#boxId').value = button.getAttribute('data-id');
            document.querySelector('#idBox').value = button.getAttribute('data-id-box');
            document.querySelector('#square').value = button.getAttribute('data-square');
            document.querySelector('#length').value = button.getAttribute('data-length');
            document.querySelector('#width').value = button.getAttribute('data-width');
            document.querySelector('#height').value = button.getAttribute('data-height');
            document.querySelector('#entrance').value = button.getAttribute('data-entrance');
            document.querySelector('#isWarm').checked = button.getAttribute('data-is-warm') === 'true';
            document.querySelector('#isElectricity').checked = button.getAttribute('data-is-electricity') === 'true';
            document.querySelector('#isWater').checked = button.getAttribute('data-is-water') === 'true';
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

///// account
function editAccount(button) {

    const id = button.getAttribute('data-id');
    //const idBox = button.getAttribute('data-id-box');

    // Создаем временный элемент для загрузки нового содержимого
    fetch('/account/fragments/account_edit_modal?id='+id)
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            document.getElementById('accountId').value = button.getAttribute('data-id');
            document.getElementById('dateStart').value = button.getAttribute('data-date-start');
            document.getElementById('dateEnd').value = button.getAttribute('data-date-end');
            document.getElementById('client').value = button.getAttribute('data-client-id');
            document.getElementById('employee').value = button.getAttribute('data-employee-id');
            document.getElementById('statusBox').value = button.getAttribute('data-status');
            document.getElementById('sumAmount').value = button.getAttribute('data-sum-amount');
            //document.querySelectorAll('input[name="boxIds"]').forEach(cb => cb.checked = true);
            //document.getElementById('boxes-form-edit').value = button.getAttribute('data-boxes');
            document.getElementById('accountModalLabel').textContent = 'Редактировать счёт';

            // Установка выбранных боксов из boxes-edit
            setCheckedBoxes_account();
            // Обновляем счётчик
            updateSelectedCount_account();
        }
    )
    .catch(error => console.error('Error loading fragment:', error));

}

///// accout_edit_modal
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

///// price
// Заполняет форму данными для редактирования
function editPrice(button) {
    const id = button.getAttribute('data-id');
    const boxId = button.getAttribute('data-box-id');
    const sum_price = button.getAttribute('data-sum_price');
    const dateStart = button.getAttribute('data-date-start');
    const dateEnd = button.getAttribute('data-date-end');

    document.getElementById('priceId').value = id;
    document.getElementById('boxId').value = boxId;
    document.getElementById('sum_price').value = sum_price;
    document.getElementById('dateStart').value = dateStart;
    document.getElementById('dateEnd').value = dateEnd;
    document.getElementById('addPriceModalLabel').textContent = 'Редактировать тариф';
}

///// client
// Заполняет форму данными для редактирования
function editClient(button) {

    const id = button.getAttribute('data-id');
    // Создаем временный элемент для загрузки нового содержимого
    fetch('/client/fragments/client_edit_modal?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            setAtributeValue_Person(button)
            document.getElementById('comment').value =  button.getAttribute('data-comment');
            document.getElementById('personModalLabel').textContent = 'Редактировать клиента';
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}

function setAtributeValue_Person(button){
    document.getElementById('objectId').value =  button.getAttribute('data-id');
    document.getElementById('firstName').value =  button.getAttribute('data-first-name');
    document.getElementById('lastName').value =  button.getAttribute('data-last-name');
    document.getElementById('birthDate').value =  button.getAttribute('data-birth-date');
    document.getElementById('phoneNumber').value =  button.getAttribute('data-phone-number');
    document.getElementById('emailAddress').value =  button.getAttribute(' data-email');
    document.getElementById('address').value =  button.getAttribute('data-address');
}


// Заполняет форму данными для редактирования
function editEmployee(button) {

    const id = button.getAttribute('data-id');
    // Создаем временный элемент для загрузки нового содержимого
    fetch('/employee/fragments/client_edit_modal?id='+id)
         .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text(); // Получаем HTML как текст
        })
        .then(html => {
            document.getElementById('content-modal-form-id').innerHTML = html;
            // Заполняем поля формы
            setAtributeValue_Person(button)
            document.getElementById('postEmployee').value = button.getAttribute('data-post');
            document.getElementById('dateStart').value = button.getAttribute('data-date-start');
            document.getElementById('dateEnd').value = button.getAttribute('data-date-end');
            document.getElementById('isFullTime').checked = button.getAttribute('data-is-full-time') === 'true';
            document.getElementById('personModalLabel').textContent = 'Редактировать сотрудника';
        }
    )
    .catch(error => console.error('Error loading fragment:', error));
}




