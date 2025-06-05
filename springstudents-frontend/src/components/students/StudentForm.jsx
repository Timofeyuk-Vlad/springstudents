import React, { useEffect, useState } from 'react'; // Добавили useState
import { Modal, Form, Input, DatePicker, message } from 'antd';
import moment from 'moment'; // Ты используешь moment, оставляем его
import api from '../../services/api';

const StudentForm = ({ visible, onCancel, student, onSuccess }) => {
    const [form] = Form.useForm();
    const [isEditing, setIsEditing] = useState(false); // Состояние для определения режима редактирования

    useEffect(() => {
        if (visible) {
            if (student && student.id) { // Проверяем student.id для определения режима редактирования
                setIsEditing(true); // Включаем режим редактирования
                form.setFieldsValue({
                    ...student,
                    // Убедимся, что moment() вызывается только если dateOfBirth есть
                    dateOfBirth: student.dateOfBirth ? moment(student.dateOfBirth, 'YYYY-MM-DD') : null,
                });
            } else {
                setIsEditing(false); // Выключаем режим редактирования (режим создания)
                form.resetFields();
            }
        }
    }, [visible, student, form]);

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            const data = {
                ...values,
                // Форматируем дату только если она не null (values.dateOfBirth может быть null, если поле было disabled и не отправлено)
                // или если это новое создание
                dateOfBirth: values.dateOfBirth ? values.dateOfBirth.format('YYYY-MM-DD') : (isEditing ? student.dateOfBirth : null)
            };

            // Если редактируем и dateOfBirth не должно меняться, можно его вообще не отправлять,
            // или бэкенд должен игнорировать это поле при обновлении, если оно не изменилось.
            // Для простоты, если isEditing, мы можем переотправить старое значение dateOfBirth, если новое не пришло.
            // Однако, если поле disabled, оно обычно не включается в values при form.validateFields().
            // Поэтому, если редактируем, возьмем дату из исходного объекта student.
            if (isEditing && student && student.dateOfBirth) {
                // Если поле dateOfBirth было disabled, оно может не прийти в values.
                // В этом случае, мы должны сохранить исходную дату.
                // Если values.dateOfBirth есть, значит оно не было disabled (что не должно быть при редактировании по заданию)
                // или мы хотим разрешить его изменение в каком-то случае.
                // По ТЗ - не менять при редактировании.
                data.dateOfBirth = student.dateOfBirth; // Всегда используем исходную дату при редактировании
            }


            if (isEditing && student && student.id) {
                await api.put(`/students/${student.id}`, data);
                message.success('Студент обновлен');
            } else {
                await api.post('/students', data);
                message.success('Студент добавлен');
            }

            onSuccess();
            onCancel(); // Закрываем модальное окно после успешной операции
        } catch (errorInfo) {
            if (errorInfo.errorFields && errorInfo.errorFields.length > 0) {
                console.error('Form Validation Failed:', errorInfo.errorFields);
                message.error('Пожалуйста, исправьте ошибки в форме.');
            } else {
                const errorMessage = errorInfo.response?.data?.message || errorInfo.message || 'Не удалось сохранить студента.';
                console.error('Submission error:', errorInfo);
                message.error(`Ошибка: ${errorMessage}`);
            }
        }
    };

    return (
        <Modal
            title={isEditing ? `Редактировать студента` : 'Добавить студента'}
            open={visible} // Для Antd v5+ используем open
            onOk={handleSubmit}
            onCancel={onCancel}
            destroyOnClose // Полезно для сброса состояния формы при закрытии
        >
            <Form form={form} layout="vertical" name="student_form_modal">
                <Form.Item
                    name="firstName"
                    label="Имя"
                    rules={[{ required: true, message: 'Пожалуйста, введите имя' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="lastName"
                    label="Фамилия"
                    rules={[{ required: true, message: 'Пожалуйста, введите фамилию' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="email"
                    label="Email"
                    rules={[
                        { required: true, message: 'Пожалуйста, введите email' },
                        { type: 'email', message: 'Некорректный email' },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="dateOfBirth"
                    label="Дата рождения"
                    // Правило required убираем, если поле может быть disabled и не отправляться
                    // Валидация @Past на бэкенде все равно сработает при создании
                    rules={isEditing ? [] : [{ required: true, message: 'Пожалуйста, выберите дату рождения' }]}
                >
                    <DatePicker
                        style={{ width: '100%' }}
                        format="YYYY-MM-DD" // Формат для отображения и парсинга moment
                        disabled={isEditing} // <--- ДЕЛАЕМ ПОЛЕ НЕАКТИВНЫМ ПРИ РЕДАКТИРОВАНИИ
                        // Чтобы запретить выбор будущих дат при создании:
                        disabledDate={current => current && current > moment().endOf('day')}
                    />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default StudentForm;