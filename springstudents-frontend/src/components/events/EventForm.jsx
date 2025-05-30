import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, DatePicker, Select, message } from 'antd';
import moment from 'moment';
import api from '../../services/api';

const { Option } = Select;

const EventForm = ({ visible, onCancel, event, onSuccess, students }) => {
    const [form] = Form.useForm();
    const [loading, setLoading] = useState(false);
    const [searchValue, setSearchValue] = useState('');

    useEffect(() => {
        if (visible) {
            if (event) {
                form.setFieldsValue({
                    ...event,
                    date: event.date ? moment(event.date) : null,
                    studentIds: event.studentIds || [],
                });
            } else {
                form.resetFields();
            }
        }
    }, [visible, event, form]);

    const validateFutureDate = (_, value) => {
        if (value && value.isBefore(moment())) {
            return Promise.reject(new Error('Необходимо ввести не наступившую дату'));
        }
        return Promise.resolve();
    };

    const handleSubmit = async () => {
        try {
            setLoading(true);
            const values = await form.validateFields();
            const payload = {
                ...values,
                date: values.date.toISOString(),
                studentIds: values.studentIds || [],
            };
            if (event) {
                await api.put(`/events/${event.id}`, payload);
                message.success('Событие обновлено');
            } else {
                await api.post('/events', payload);
                message.success('Событие создано');
            }
            onSuccess();
            onCancel();
        } catch (error) {
            if (error.response?.status === 400) {
                message.error('Ошибка: ' + (error.response.data.message || 'Неверные данные'));
            } else {
                message.error('Ошибка: ' + (error.message || 'Произошла ошибка'));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (value) => {
        setSearchValue(value);
    };

    const filteredStudents = students.filter(student => {
        const fullName = `${student.firstName} ${student.lastName}`.toLowerCase();
        return fullName.includes(searchValue.toLowerCase());
    });

    return (
        <Modal
            open={visible}
            title={event ? 'Редактировать событие' : 'Создать событие'}
            onCancel={onCancel}
            onOk={handleSubmit}
            confirmLoading={loading}
            okText={event ? 'Сохранить' : 'Создать'}
            cancelText="Отмена"
            destroyOnClose
        >
            <Form
                form={form}
                layout="vertical"
                initialValues={{
                    name: '',
                    date: null,
                    studentIds: [],
                }}
            >
                <Form.Item
                    name="name"
                    label="Название"
                    rules={[
                        { required: true, message: 'Введите название события' },
                    ]}
                >
                    <Input placeholder="Введите название события" />
                </Form.Item>

                <Form.Item
                    name="date"
                    label="Дата и время"
                    rules={[
                        { required: true, message: 'Выберите дату и время' },
                        { validator: validateFutureDate },
                    ]}
                >
                    <DatePicker
                        showTime
                        style={{ width: '100%' }}
                        format="YYYY-MM-DD HH:mm"
                        disabledDate={current => current && current < moment().startOf('day')}
                        placeholder="Выберите дату и время"
                    />
                </Form.Item>

                <Form.Item
                    name="studentIds"
                    label="Участники"
                    rules={[
                        {
                            required: true,
                            type: 'array',
                            min: 1,
                            message: 'Должен быть хотя бы один участник',
                        },
                    ]}
                >
                    <Select
                        mode="multiple"
                        placeholder="Начните вводить имя участника"
                        showSearch
                        onSearch={handleSearch}
                        filterOption={false}
                        style={{ width: '100%' }}
                    >
                        {filteredStudents.map(student => (
                            <Option key={student.id} value={student.id}>
                                {student.firstName} {student.lastName}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EventForm;