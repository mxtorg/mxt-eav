import { render, screen } from '@testing-library/react';
import App from './App';

test('renders MXT EAV Management System header', () => {
  render(<App />);
  const headerElement = screen.getByText(/MXT EAV 管理系统/i);
  expect(headerElement).toBeInTheDocument();
});

test('renders tabs for navigation', () => {
  render(<App />);
  expect(screen.getByText('实体类型')).toBeInTheDocument();
  expect(screen.getByText('属性管理')).toBeInTheDocument();
  expect(screen.getByText('实体管理')).toBeInTheDocument();
});
