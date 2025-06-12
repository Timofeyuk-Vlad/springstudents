import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, Select, Button, message } from 'antd';
import { fetchStudents, createBarter, updateBarter } from '../../services/api';

const { Option } = Select;

const BarterForm = ({ visible, onCancel, barter, onSuccess }) => {
    const [form] = Form.useForm();
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [filteredStudents, setFilteredStudents] = useState([]);
    const [searchValue, setSearchValue] = useState('');

    useEffect(() => {
        if (visible) {
            loadStudents();
            if (barter) {
                form.setFieldsValue(barter);
            } else {
                resetForm();
            }
        }
    }, [visible, barter, form]);

    const loadStudents = async () => {
        setLoading(true);
        try {
            const { data } = await fetchStudents();
            setStudents(data);
            setFilteredStudents(data);
        } catch (error) {
            message.error('Не удалось загрузить список студентов');
        } finally {
            setLoading(false);
        }
    };

    const resetForm = () => {
        form.resetFields();
        form.setFieldsValue({
            status: 'active'
        });
        setSearchValue('');
        setFilteredStudents(students);
    };

    const handleStudentSearch = (value) => {
        setSearchValue(value);
        if (value) {
            const filtered = students.filter(student =>
                `${student.firstName} ${student.lastName}`
                    .toLowerCase()
                    .includes(value.toLowerCase())
            );
            setFilteredStudents(filtered);
        } else {
            setFilteredStudents(students);
        }
    };

    const clearSearch = () => {
        setSearchValue('');
        setFilteredStudents(students);
        form.setFieldsValue({
            studentId: undefined
        });
    };

    const handleSubmit = async () => {
        try {
            const values = await form.validateFields();
            if (barter) {
                await updateBarter(barter.id, values);
                message.success('Обмен успешно обновлён');
            } else {
                await createBarter(values);
                message.success('Новый обмен успешно создан');
            }
            onSuccess();
            onCancel();
        } catch (error) {
            message.error('Произошла ошибка: ' + (error.response?.data?.message || error.message));
        }
    };

    return (
        <Modal
            title={barter ? 'Редактирование обмена' : 'Создание нового обмена'}
            visible={visible}
            onOk={handleSubmit}
            onCancel={onCancel}
            confirmLoading={loading}
            okText={barter ? "Сохранить" : "Создать"}
            cancelText="Отмена"
            footer={[
                <Button key="Сброс" onClick={resetForm}>
                    Сбросить форму
                </Button>,
                <Button key="cancel" onClick={onCancel}>
                    Отмена
                </Button>,
                <Button
                    key="submit"
                    type="primary"
                    onClick={handleSubmit}
                    loading={loading}
                >
                    {barter ? "Сохранить" : "Создать"}
                </Button>,
            ]}
        >
            <Form form={form} layout="vertical">
                <Form.Item
                    name="item"
                    label="Предмет обмена"
                    rules={[{ required: true, message: 'Пожалуйста, укажите предмет обмена' }]}
                >
                    <Input placeholder="Например, учебник по математике" />
                </Form.Item>

                <Form.Item
                    name="description"
                    label="Подробное описание"
                >
                    <Input.TextArea placeholder="Опишите состояние предмета и условия обмена" />
                </Form.Item>

                <Form.Item
                    name="status"
                    label="Статус обмена"
                    rules={[{ required: true, message: 'Пожалуйста, выберите статус' }]}
                >
                    <Select disabled={!barter}>
                        <Option value="active">Активен</Option>
                        <Option value="closed" disabled={!barter}>
                            Завершён
                        </Option>
                    </Select>
                </Form.Item>

                <Form.Item
                    name="studentId"
                    label="Участник обмена"
                    rules={[{ required: true, message: 'Пожалуйста, выберите участника' }]}
                    extra={
                        <div style={{ marginTop: 8 }}>
                            {searchValue && (
                                <Button
                                    type="link"
                                    onClick={clearSearch}
                                    size="small"
                                >
                                    Очистить фильтр
                                </Button>
                            )}
                        </div>
                    }
                >
                    <Select
                        showSearch
                        optionFilterProp="children"
                        placeholder="Выберите студента из списка"
                        onSearch={handleStudentSearch}
                        filterOption={false}
                    >
                        {filteredStudents.map((student) => (
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

export default BarterForm;