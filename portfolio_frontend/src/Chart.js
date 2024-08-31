import React from 'react';
import { Bar, Doughnut, PolarArea } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  RadialLinearScale,
  Legend
} from 'chart.js';

const totalAmountPlugin = {
  id: 'totalAmount',
  beforeDraw: (chart) => {
    const ctx = chart.ctx;
    const { width, height } = chart;
    const center = { x: width / 2, y: height / 2 };
    const datasets = chart.data.datasets;

    if (datasets.length) {
      const total = Math.round(datasets[0].data.reduce((sum, value) => sum + value, 0));
      ctx.save();
      const fontSize = (height / 100) * 5;
      ctx.font = `bold ${fontSize}px Arial`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillStyle = '#fff';
      ctx.fillText(`${total} €`, center.x, center.y);
      ctx.restore();
    }
  }
};

const segmentLabelPlugin = {
  id: 'segmentLabelPlugin',
  afterDatasetsDraw: chart => {
    const ctx = chart.ctx;
    chart.getDatasetMeta(0).data.forEach((arc, i) => {
      const centerPoint = arc.getCenterPoint();
      const total = chart.data.datasets[0].data.reduce((sum, val) => sum + val, 0);
      const percentage = ((arc.circumference / (2 * Math.PI)) * 100).toFixed(2);
      const label = chart.data.labels[i];
      const value = chart.data.datasets[0].data[i].toFixed(2);

      const texts = [`${label}`, `${value} €`, `${percentage}%`];
      const { width, height } = chart;
      const fontSize = (height / 100) * 2.5;
      ctx.font = `bold ${fontSize}px Arial`;
      ctx.fillStyle = '#fff';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';

      texts.forEach((text, index) => {
        const offset = (fontSize * 1.5) * (index - (texts.length - 1) / 2);
        ctx.fillText(text, centerPoint.x, centerPoint.y + offset);
      });
    });
  }
};

ChartJS.register(
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  RadialLinearScale,
  totalAmountPlugin,
  segmentLabelPlugin
);

function ChartComponent({ type, data, options }) {
  const ChartComponentMap = {
    Bar: Bar,
    Doughnut: Doughnut,
    PolarArea: PolarArea
  };

  const SelectedChartComponent = ChartComponentMap[type];

  const defaultOptions = {
    plugins: {
      totalAmount: true,
      legend: { display: true },
      tooltip: {
        enabled: true,
        callbacks: {
          label: function (context) {
            const dataset = context.dataset;
            const sortedAssets = dataset.sortedAssets;  // Zugriff auf die zusätzlichen Informationen
            const asset = sortedAssets[context.dataIndex];


            const label = context.chart.data.labels[context.dataIndex];
            const value = context.raw.toFixed(2);
            const quantity = asset.quantity.toFixed(2);
            const pricePerUnit = asset.asset.lastValue.toFixed(2);
            return [
              `Anzahl: ${quantity}`,
              `Kurs: ${pricePerUnit} €`,
            ];
          },
          
        }
      },
      segmentLabelPlugin: true,
      responsive: true,
      maintainAspectRatio: false
    },
    elements: {
        arc: {
          borderWidth: 0.75 // Hier kannst du die Breite des Rahmens anpassen
        }
      },
    ...options
  };

  return (
    <div className="chart-container">
      <SelectedChartComponent data={data} options={defaultOptions} />
    </div>
  );
}

export default ChartComponent;
