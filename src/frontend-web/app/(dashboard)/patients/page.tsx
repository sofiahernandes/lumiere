"use client";

import { useMemo, useState } from "react";
import {
  initialPatients,
  initialSchedule,
  type Patient,
} from "../../lib/mock-data";

export default function PatientsPage() {
  const [patients, setPatients] = useState<Patient[]>(initialPatients);
  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState(initialPatients[0]?.id ?? "");
  const [schedules, setSchedules] = useState(initialSchedule);
  const [scheduleForm, setScheduleForm] = useState({
    exerciseName: "",
    frequency: "",
    orientation: "",
  });

  const filteredPatients = useMemo(() => {
    return patients.filter((patient) => {
      const fullName = `${patient.firstName} ${patient.lastName}`.toLowerCase();
      return fullName.includes(query.toLowerCase());
    });
  }, [patients, query]);

  const selectedPatient =
    patients.find((patient) => patient.id === selectedId) ??
    filteredPatients[0];
  const patientSchedules = schedules.filter(
    (item) => item.patientId === selectedPatient?.id,
  );

  function deletePatient(id: string) {
    setPatients((prev) => prev.filter((patient) => patient.id !== id));
    setSchedules((prev) => prev.filter((item) => item.patientId !== id));
    if (selectedId === id) {
      const next = patients.find((patient) => patient.id !== id);
      setSelectedId(next?.id ?? "");
    }
  }

  function submitSchedule(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedPatient) return;
    if (
      !scheduleForm.exerciseName ||
      !scheduleForm.frequency ||
      !scheduleForm.orientation
    )
      return;

    setSchedules((prev) => [
      {
        id: `SCH-${Date.now()}`,
        patientId: selectedPatient.id,
        exerciseName: scheduleForm.exerciseName,
        frequency: scheduleForm.frequency,
        orientation: scheduleForm.orientation,
      },
      ...prev,
    ]);

    setScheduleForm({ exerciseName: "", frequency: "", orientation: "" });
  }

  return (
    <section className="grid grid-cols-4 gap-4 md:grid-cols-12">
      <header className="col-span-full pt-6 px-4">
        <h1 className="font-display text-4xl">Acompanhar Pacientes</h1>
      </header>

      <div className="panel col-span-4 p-5 md:col-span-7">
        <div className="grid grid-cols-4 gap-3 md:grid-cols-12">
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Buscar por nome"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-12 placeholder:text-neutral-700"
          />
        </div>

        <div className="mt-4 overflow-x-auto max-h-68 no-scrollbar">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-slate-200">
                <th className="py-2">Paciente</th>
              </tr>
            </thead>
            <tbody>
              {filteredPatients.map((patient) => (
                <tr
                  key={patient.id}
                  className="flex justify-between border-b border-slate-100"
                >
                  <td className="py-3">
                    <button
                      onClick={() => setSelectedId(patient.id)}
                      className="hover:opacity-70 transition duration-300 ease-in-out"
                    >
                      {patient.firstName} {patient.lastName}
                    </button>
                  </td>
                  <td className="py-3">
                    <div className="flex gap-2">
                      <button
                        onClick={() => deletePatient(patient.id)}
                        className="rounded-md bg-light-salmon px-3 py-1 hover:opacity-70 transition duration-300 ease-in-out"
                      >
                        Excluir
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="panel col-span-4 p-5 md:col-span-5 md:flex md:flex-col">
        <h2 className="text-xl">Prontuário e Evolução</h2>
        {selectedPatient ? (
          <div className="mt-3 flex flex-1 flex-col justify-between">
            <div className="flex flex-col gap-y-3">
              <p>
                <span className="font-semibold">Paciente:</span>{" "}
                {selectedPatient.firstName} {selectedPatient.lastName}
              </p>
              <p>
                <span className="font-semibold">Diagnóstico:</span>{" "}
                {selectedPatient.diagnosis}
              </p>
              <p>
                <span className="font-semibold">Última sessão:</span>{" "}
                {selectedPatient.lastSession}
              </p>
              <p>
                <span className="font-semibold">Escala média de dor:</span>{" "}
                {selectedPatient.painLevel}/10
              </p>
              <div>
                <p className="font-semibold">Aderência ao plano</p>
                <div className="h-3 rounded-full bg-neutral-200">
                  <div
                    className="h-3 rounded-full bg-black/70"
                    style={{ width: `${selectedPatient.adherence}%` }}
                  />
                </div>
                <p className="text-slate-600">
                  {selectedPatient.adherence}% dos exercícios foram completados
                </p>
              </div>
            </div>
            <button className="rounded-md bg-dark-blue px-4 py-2 text-white w-full hover:opacity-70 transition duration-300 ease-in-out">
              Acessar prontuário completo
            </button>
          </div>
        ) : (
          <p className="mt-3  text-slate-500">
            Selecione um paciente para visualizar o prontuário.
          </p>
        )}
      </div>

      <div className="panel col-span-4 p-5 md:col-span-12">
        <h2 className="text-xl">Calendário do Paciente Selecionado</h2>
        <form
          onSubmit={submitSchedule}
          className="mt-3 grid grid-cols-4 gap-3 md:grid-cols-12"
        >
          <input
            value={scheduleForm.exerciseName}
            onChange={(event) =>
              setScheduleForm((prev) => ({
                ...prev,
                exerciseName: event.target.value,
              }))
            }
            placeholder="Exercício"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-3"
          />
          <input
            value={scheduleForm.frequency}
            onChange={(event) =>
              setScheduleForm((prev) => ({
                ...prev,
                frequency: event.target.value,
              }))
            }
            placeholder="Frequência"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-3"
          />
          <input
            value={scheduleForm.orientation}
            onChange={(event) =>
              setScheduleForm((prev) => ({
                ...prev,
                orientation: event.target.value,
              }))
            }
            placeholder="Orientações"
            className="col-span-4 rounded-md border border-slate-300 px-3 py-2 md:col-span-4"
          />
          <button
            className="col-span-4 rounded-md bg-dark-blue px-4 py-2 font-semibold text-white md:col-span-2 hover:opacity-70 transition duration-300 ease-in-out"
            type="submit"
          >
            Adicionar
          </button>
        </form>

        <div className="mt-4 grid grid-cols-4 gap-3 md:grid-cols-12">
          {patientSchedules.length > 0 ? (
            patientSchedules.map((item) => (
              <article
                key={item.id}
                className="col-span-4 rounded-md border border-slate-200 bg-slate-50 p-3 md:col-span-4"
              >
                <p className="font-semibold">{item.exerciseName}</p>
                <p className=" text-slate-600">Frequência: {item.frequency}</p>
              </article>
            ))
          ) : (
            <p className="col-span-4  text-slate-500 md:col-span-12">
              Nenhum exercicio associado para o paciente selecionado.
            </p>
          )}
        </div>
      </div>
    </section>
  );
}
