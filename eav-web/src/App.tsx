import { useState } from 'react';
import { Layout } from './components/Layout';
import { EntityTypePage } from './pages/EntityTypePage';
import { AttributePage } from './pages/AttributePage';
import { EavEntityPage } from './pages/EavEntityPage';

type TabType = 'entity-types' | 'attributes' | 'entities';

function App() {
  const [activeTab, setActiveTab] = useState<TabType>('entity-types');

  const tabs: { key: TabType; label: string }[] = [
    { key: 'entity-types', label: '实体类型' },
    { key: 'attributes', label: '属性管理' },
    { key: 'entities', label: '实体管理' },
  ];

  const renderContent = () => {
    switch (activeTab) {
      case 'entity-types':
        return <EntityTypePage />;
      case 'attributes':
        return <AttributePage />;
      case 'entities':
        return <EavEntityPage />;
      default:
        return null;
    }
  };

  return (
    <Layout>
      <div style={styles.tabs}>
        {tabs.map((tab) => (
          <button
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            style={{
              ...styles.tabBtn,
              ...(activeTab === tab.key ? styles.tabBtnActive : {}),
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>
      <div style={styles.content}>{renderContent()}</div>
    </Layout>
  );
}

const styles = {
  tabs: {
    display: 'flex',
    gap: '0.5rem',
    marginBottom: '1.5rem',
    borderBottom: '1px solid #dee2e6',
  },
  tabBtn: {
    padding: '0.75rem 1.25rem',
    background: 'none',
    border: 'none',
    borderRadius: '4px 4px 0 0',
    cursor: 'pointer',
    fontSize: '1rem',
    fontWeight: 500,
    color: '#6c757d',
    ':hover': {
      backgroundColor: '#f8f9fa',
      color: '#495057',
    },
  },
  tabBtnActive: {
    backgroundColor: '#fff',
    color: '#007bff',
    border: '1px solid #dee2e6',
    borderBottomColor: '#fff',
    marginBottom: '-1px',
  },
  content: {
    backgroundColor: '#fff',
    borderRadius: '8px',
    padding: '1.5rem',
    boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
  },
};

export default App;
