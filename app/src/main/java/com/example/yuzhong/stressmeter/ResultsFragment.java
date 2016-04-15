package com.example.yuzhong.stressmeter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int numberOfPoints;
    ArrayList<String> mStressLevel;
    ArrayList<String> mTime;
    private int maxStressLevel = 7;
    private int minStressLevel = 0;
    TableLayout dataTable;

    float[][] stressRecord;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = true;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    // TODO: Rename and change types and number of parameters
    public static ResultsFragment newInstance(String param1, String param2) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        // declare Table
        dataTable = (TableLayout)rootView.findViewById(R.id.datatable);

        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        // Generate some random values.
        generateValues();

        //Generate the line in the chart
        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        //set the chart size and axis etc
        resetViewport();

        //fill the table with the data of csv file
        setTableData();

        return rootView;
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected point: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void generateValues() {

        //get file from csv file
        String fileName = "stressData.csv";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(file);

            BufferedReader r = new BufferedReader(new InputStreamReader(fIn));

            //read content of the file
            mStressLevel = new ArrayList<String>();
            mTime = new ArrayList<String>();

            String strLine = null;

            while ((strLine = r.readLine()) != null)   {
                String[] dataValue = strLine.split(",");
                mStressLevel.add(dataValue[1]);
                mTime.add(dataValue[0]);
                maxStressLevel = Math.max(maxStressLevel, Integer.parseInt(dataValue[1]));
                minStressLevel = Math.min(minStressLevel, Integer.parseInt(dataValue[1]));
            }

            r.close();

            numberOfPoints = mStressLevel.size();

            stressRecord = new float[numberOfLines][numberOfPoints];

            for (int i = 0; i < numberOfLines; ++i) {
                for (int j = 0; j < numberOfPoints; ++j) {
                    stressRecord[i][j] = (float) Integer.parseInt(mStressLevel.get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateData() {

        //create lines instance to hold the lines of points, here we only need to draw one line
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, stressRecord[i][j]));
            }

            //set the line properties
            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Instances");
                axisY.setName("Stress Level");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,40)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = minStressLevel;
        v.top = maxStressLevel;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);

    }

    private void setTableData(){

        //set the table content data
        for(int i = 0; i < numberOfPoints; i++) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setWeightSum(1);
            for(int j = 0; j < 2; j++)
            {
                // new TextView
                TextView tableColumn = new TextView(getActivity());
                String value = null;
                if(j == 0){
                    value = mTime.get(i);
                    tableColumn.setText(value);
                }else {
                    value = mStressLevel.get(i);
                    tableColumn.setText(value);
                }
                if(i % 2 == 0)
                    tableColumn.setBackgroundColor(Color.LTGRAY);
                tableRow.addView(tableColumn);
            }
            // add tableRow to the tableLayout
            dataTable.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }
}
